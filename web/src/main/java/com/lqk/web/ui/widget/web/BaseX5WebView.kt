package com.lqk.web.ui.widget.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.widget.Toast
import com.lqk.web.ui.widget.web.script.NativeJavaScriptInterface
import com.tencent.smtt.export.external.extension.proxy.ProxyWebChromeClientExtension
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @remarks 网页 WebView 封装
 */
open class BaseX5WebView : WebView, NativeJavaScriptInterface.OnJsNeedCallBack {

    override fun onSucceed(params:  LinkedHashMap<String, Any>, callbackName: String) {

    }

    override fun onFailed(code: Int, msg: String, callbackName: String) {

    }

    // <editor-fold desc="构造函数">
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, privateBrowsing: Boolean)
            : super(context, attrs, defStyleAttr, privateBrowsing) {
        initWebView()
    }
    // </editor-fold>

    // 提供给 h5调用 Java 函数的对象类, 需要挂载到 WebView 上
    private lateinit var native: NativeJavaScriptInterface

    private var webCallback: WebCallBack? = null
    fun initWebCallBack(l: WebCallBack) {
        this.webCallback = l
    }

    /**
     * 初始化 WebView 配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        // 网页调试模式
        WebView.setWebContentsDebuggingEnabled(true)
        // 5.0 以上，开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }
        // 屏幕自适应
        this.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        this.settings.loadWithOverviewMode = true
        this.settings.useWideViewPort = true
        // 允许 js 代码
        /// Android 4.3 调用reload 会回调多次 WebChromeClient.onJsPrompt()
        this.settings.javaScriptEnabled = true
        // 删除 有安全漏洞的 挂载对象
        // 移除部分系统 JavaScript 接口
        removeJavaScriptInterfaces(this)
        // 允许 SessionStorage/ LocalStorage 存储
        this.settings.domStorageEnabled = true
        // 默认文本编码格式
        this.settings.defaultTextEncodingName = "utf-8"
        // 缩放
        this.settings.displayZoomControls = false
        this.settings.builtInZoomControls = false
        // 文字缩放
        this.settings.textZoom = 100
        // 缓存 10M API 18之后 ，系统自动处理
        this.settings.setAppCacheMaxSize(10 * 1024 * 1024L)
        // 缓存
        this.settings.setAppCacheEnabled(true)
        // 缓存地址
        this.settings.setAppCachePath(context.getDir("appcache", 0).path)
        // 文件File 协议
        this.settings.allowFileAccess = true
        // 保存密码
        this.settings.savePassword = false
        // 设置 UA todo
        this.settings.userAgentString =
            "${this.settings.userAgentString};${context.packageName};webview;${extUserAgent()}"

        // 自动加载 图片
        this.settings.loadsImagesAutomatically = true

        // WebViewClient
        this.webViewClient = initWebViewClient()
        // WebChromeClient
        this.webChromeClient = initWebChromeClient()
        // 前进后退
//        this.webChromeClientExtension = initWebChromeClientExtension()
        // 挂载交互对象
        native = initJavaScriptInterface()
        this.loadNativeJavaScriptInterface<NativeJavaScriptInterface>(mountWebString(), native)
    }

    /**
     * 挂载到 WebView -> web 中 window.xxx 的 xxx 对象的名称
     */
    open fun mountWebString(): String {
        return "android"
    }

    /**
     * 拓展 UA 字段
     */
    open fun extUserAgent(): String {
        return ""
    }

    /**
     * 生成需要挂载到 h5 的对象
     */
    open fun initJavaScriptInterface(): NativeJavaScriptInterface {
        return NativeJavaScriptInterface(this.webCallback, this)
    }

    /**
     * 注入一段 JS 这种方式需要 WebView 开启加载本地文件
     * @param fileName assets下的文件 名称(带后缀)
     * 注入后
     * <script src="file:///android_asset/xxx" id="lqk_inject_script"></script>
     */
    private fun loadJsFromLocal(fileName: String) {
        loadUrl(
            "javascript:try{" +
                    "if(document.head && !document.getElementById('lqk_inject_script')) {\n" +
                    "var injectScript = document.createElement('script');" +
                    "injectScript.src='file:///android_asset/$fileName';" +
                    "injectScript.id='lqk_inject_script';" +
                    "document.head.appendChild(injectScript);" +
                    "}" +
                    "}catch(e){}"
        )
    }

    /**
     * 读取js文件 转为String
     * 可以提供给子类复写, 替换相关关键字
     */
    open fun readStringFromJs(fileName: String): String {
        val stringBuilder = StringBuilder()
        return try {
            val inputReader = InputStreamReader(resources.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            var line: String?
            while (bufReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufReader.close()
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun loadAssetsJsFile(fileName: String) {
        return
    }

    /**
     * @param funcName 方法名
     * @param strJS 参数: json 字符串类型
     * @param callback 执行 js 代码后的回调
     *
     */
    private fun loadJs(funcName: String, strJS: String, callback: ValueCallback<String>? = null) {
        evaluateJavascript("javascript:$funcName('$strJS')", callback)
    }

    /**
     * 加载网页
     */
    fun loadHtmlOfUrl(url: String) {
        // todo 记录网页
        this.loadUrl(url)
    }

    /**
     * 重新加载网页
     */
    fun reloadUrl() {
        this.reload()
    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        //清除cookie
        CookieManager.getInstance().removeAllCookies(null);
        //清除storage相关缓存
        WebStorage.getInstance().deleteAllData()
        //清除用户密码信息
        WebViewDatabase.getInstance(context).clearUsernamePassword()
        //清除httpauth信息
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword()
        //清除表单数据
        WebViewDatabase.getInstance(context).clearFormData()
        //清除页面icon图标信息
        WebIconDatabase.getInstance().removeAllIcons()
        //删除地理位置授权，也可以删除某个域名的授权（参考接口类）
        GeolocationPermissions.getInstance().clearAll()
    }

    /**
     * 返回
     */
    fun back() {
        // 如果可以返回
        if (canGoBack()) {
            this.goBack()
        }
    }

    /**
     * 前进
     */
    fun forward() {
        if (canGoForward()) {
            this.goForward()
        }
    }

    /**
     * 防止远程执行漏洞
     */
    private fun removeJavaScriptInterfaces(webView: WebView) {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_")
                webView.removeJavascriptInterface("accessibility")
                webView.removeJavascriptInterface("accessibilityTraversal")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 添加 js 回调
     */
    @SuppressLint("JavascriptInterface")
    fun addJavaScript(native: Any, key: String) {
        this.addJavascriptInterface(native, key)
    }

    //<editor-fold desc="对外接口">
    /**
     * 添加 JavaScript Native 调用类
     */
    @SuppressLint("JavascriptInterface")
    fun <T : Any> loadNativeJavaScriptInterface(name: String, t: T) {
        this.addJavascriptInterface(t, name)
    }

    private fun initWebChromeClientExtension(): ProxyWebChromeClientExtension {
        return object : ProxyWebChromeClientExtension() {
            // 页面前进后退切换完成事件通知
            override fun onBackforwardFinished(p0: Int) {
                super.onBackforwardFinished(p0)
            }

            /**
             * callback：处理后的回调；
             * schemePlusHost：域名；
             * username：用户名；
             * password：密码；
             * nameElement：用户名输入框名称；
             * passwordElement：密码输入框名称；
             * isReplace：是否是替换操作
             */
            override fun onSavePassword(
                p0: android.webkit.ValueCallback<String>?,
                p1: String?,
                p2: String?,
                p3: String?,
                p4: String?,
                p5: String?,
                p6: Boolean
            ): Boolean {
                return super.onSavePassword(p0, p1, p2, p3, p4, p5, p6)
            }
        }
    }

    /**
     * WebChromeClient
     */
    private fun initWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                p1?.let {
                    this@BaseX5WebView.webCallback?.onTitle(it)
                }
            }

            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            // web alert 方法
            override fun onJsAlert(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
                return super.onJsAlert(p0, p1, p2, p3)
            }

            // 网页打印信息
            override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                Toast.makeText(this@BaseX5WebView.context, "${p0?.message()}", Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            // 关闭 WebView
            override fun onCloseWindow(p0: WebView?) {
                super.onCloseWindow(p0)
            }


            //  文件上传功能 4.3+ API18


            // 文件上传功能 5.0+ API21
            override fun onShowFileChooser(
                p0: WebView?,
                p1: ValueCallback<Array<Uri>>?,
                p2: FileChooserParams?
            ): Boolean {
                return super.onShowFileChooser(p0, p1, p2)
            }
        }
    }

    /**
     * WebViewClient
     * 页面加载进度
     */
    private fun initWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onReceivedHttpAuthRequest(
                p0: WebView?,
                p1: HttpAuthHandler?,
                p2: String?,
                p3: String?
            ) {
                super.onReceivedHttpAuthRequest(p0, p1, p2, p3)
                //首先判断是否可以重复使用对应用户名和密码如果可以则获取已保存的密码（获取成功后直接使用）
                //如果不允许重复使用用户名和密码或者未保存用户名和密码则需要提示用户输入
                //用户输入用户名和密码后可以将对应数据保存
            }

            // 地址栏 url 变化
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            override fun shouldOverrideUrlLoading(p0: WebView?, p1: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            // 加载资源
            // 网页请求数据
            // 网页的 post 等网络请求操作
//            override fun shouldInterceptRequest(p0: WebView?, p1: String?): WebResourceResponse {
//                return super.shouldInterceptRequest(p0, p1)
//            }

//            override fun shouldInterceptRequest(
//                p0: WebView?,
//                p1: WebResourceRequest?
//            ): WebResourceResponse {
//                return super.shouldInterceptRequest(p0, p1)
//            }

            override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
                super.onPageStarted(p0, p1, p2)
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                // 页面加载完成之后, 注入js
            }

            override fun onLoadResource(p0: WebView?, p1: String?) {
                super.onLoadResource(p0, p1)
            }

        }
    }
    //</editor-fold>


    /**
     * 网页回调
     */
    interface OnWebPageLoadListener {}

    /**
     * 网页调用方法的回调
     */
    interface OnWebFunCListener {
        fun onNative(funcName: String, params: LinkedHashMap<String, Any>, callback: Any)
    }


}