package com.lqk.web.ui.widget.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import com.lqk.network.OkHttpUtil
import com.lqk.web.ui.widget.web.script.NativeJavaScriptInterface
import com.lqk.web.utils.VideoOrImageUtil
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader

/**
 * Android 原生 WebView 封装
 */
open class BaseWebView : WebView, NativeJavaScriptInterface.OnJsNeedCallBack {
    companion object {
        private const val TAG = "BaseWebView"
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

    private var strMountObj = "android"

    // 提供给 h5调用 Java 函数的对象类, 需要挂载到 WebView 上
    private lateinit var native: NativeJavaScriptInterface

    // 监听
    private var webCallback: WebCallBack? = null

    /**
     * 绑定 web 事件监听 - 提供给外部
     */
    fun initWebCallBack(l: WebCallBack) {
        this.webCallback = l
    }

    /**
     * 初始化 WebView 配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        // 网页调试模式
        setWebContentsDebuggingEnabled(true)
        // 5.0 以上，开启混合模式加载 (http/https)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 在这种模式下，WebView 将允许安全源从任何其他源加载内容，即使该源不安全。
            // 这是 WebView 最不安全的操作模式，应用程序不应设置此模式
            this.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
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

//        // 缓存 10M API 18之后 ，系统自动处理
//        this.settings.setAppCacheMaxSize(10 * 1024 * 1024L)
//        // 缓存地址
//        this.settings.setAppCachePath(context.getDir("appcache", 0).path)
//        // 缓存
//        this.settings.setAppCacheEnabled(true)

        // 文件File 协议
        this.settings.allowFileAccess = true
        // 安全策略
//        this.settings.allowFileAccessFromFileURLs = true
//        this.settings.allowUniversalAccessFromFileURLs = true

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
        // 前进后退结束监听
        // this.webChromeClientExtension = initWebChromeClientExtension()
        // 挂载交互对象
        // mountObj()
    }

    // <editor-fold desc="">
    /**
     * 参数为 json 格式字符串
     */
    override fun onSucceed(params: LinkedHashMap<String, Any>, callbackName: String) {
        // 构建返回对象
        // code msg callbackName params

        // 调用 web
        loadJs(
            "hfiJSBridge.onWeb",
            WebBean.create(callbackName = callbackName, params = params).toJson(),
            null
        )
    }

    override fun onFailed(code: Int, msg: String, callbackName: String) {
        // 构建返回对象
        // code msg callbackName params
        loadJs(
            "hfiJSBridge.onWeb",
            WebBean.create(code = code, msg = msg, callbackName = callbackName).toJson(),
            null
        )
    }

    // </editor-fold>

    /**
     * 挂载对象
     */
    fun mountObj() {
        native = initJavaScriptInterface()
        this.loadNativeJavaScriptInterface<NativeJavaScriptInterface>(mountWebString(), native)
    }

    /**
     * 挂载到 WebView -> web 中 window.xxx 的 xxx 对象的名称
     */
    open fun mountWebString(): String {
        return strMountObj
    }

    /**
     * 需要重新设置 挂载对象名称
     */
    fun resetMountStr(str: String) {
        this.strMountObj = str
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
    private fun loadJsFromLocal(id: String, fileName: String) {
        loadUrl(
            "javascript:try{" +
                    "if(document.head && !document.getElementById('${id}_inject_script')) {\n" +
                    "var injectScript = document.createElement('script');" +
                    "injectScript.src='file:///android_asset/$fileName';" +
                    "injectScript.id='${id}_inject_script';" +
                    "document.head.appendChild(injectScript);" +
                    "}" +
                    "}catch(e){}"
        )
    }

    // <script src="https://unpkg.com/vconsole@latest/dist/vconsole.min.js"></script>
    // <script>
    //   // VConsole will be exported to `window.VConsole` by default.
    //   var vConsole = new window.VConsole();
    // </script>

    private fun loadVConsole() {
        loadUrl(
            "javascript:try{" +
                    "if(document.head && !document.getElementById('console_inject_script')) {\n" +
                    "var injectScript = document.createElement('script');" +
                    "injectScript.src='https://unpkg.com/vconsole@latest/dist/vconsole.min.js';" +
                    "injectScript.id='console_inject_script_inject_script';" +
                    "document.head.appendChild(injectScript);" +
                    "}" +
                    "}catch(e){}"
        )
        loadUrl("javascript: !function(){var vConsole = new window.VConsole();}()")
    }

    private fun loadJsFromLocal(fileName: String) {
        this.evaluateJavascript("javascript:${readStringFromJs(fileName)}") {
            Log.d(TAG, "loadJsFromLocal: $it")
        }
    }

    /**
     * 读取 assets 文件夹中的 js文件 转为 String
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

    /**
     * @param funcName 方法名
     * @param strJS 参数: json 字符串类型
     * @param callback 执行 js 代码后的回调
     * 需要线程切换
     *
     */
    fun loadJs(funcName: String, strJS: String, callback: ValueCallback<String>? = null) {
        evaluateJavascript("javascript:$funcName('$strJS')", callback)
    }

    // <editor-fold desc="网页基本操作">
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

    // </editor-fold>

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

    /**
     * WebChromeClient
     */
    private fun initWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            // 标题
            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                p1?.let {
                    this@BaseWebView.webCallback?.onTitle(it)
                }
            }

            // 图标
            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                super.onReceivedIcon(view, icon)
            }

            // prompt 输入弹框
            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            // web alert 确认
            override fun onJsAlert(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
                return super.onJsAlert(p0, p1, p2, p3)
            }

            // web confirm 确认/取消
            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            // 网页打印信息
            override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                Log.d(TAG, "onConsoleMessage: ${p0?.message()}")
                return true
            }

            // 关闭 WebView
            override fun onCloseWindow(p0: WebView?) {
                super.onCloseWindow(p0)
                webCallback?.close()
            }

            //  文件上传功能 4.3+ API18

            // 文件上传功能 5.0+ API21
            override fun onShowFileChooser(
                p0: WebView?,
                valueCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // 使用 PictureSelector 选择视频/图片
                // h5 <input type="file"> 选择图片
                fileChooserParams?.let {
                    val acceptType = it.acceptTypes
                    val title = it.title
                    val mode = it.mode
                    when (acceptType[0]) {
                        "image/*" -> {
                            VideoOrImageUtil.openImage(this@BaseWebView.context, object :
                                OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    result?.let { m ->
                                        val uriList: MutableList<Uri> = mutableListOf()
                                        m.forEach { localMedia ->
                                            uriList.add(Uri.fromFile(File(localMedia.realPath)))
                                        }
                                        valueCallback?.onReceiveValue(uriList.toTypedArray())
                                    }
                                }

                                override fun onCancel() {
                                    valueCallback?.onReceiveValue(arrayOf())
                                }
                            })
                        }
                        "video/*" -> {
                            VideoOrImageUtil.openVideo(this@BaseWebView.context, object :
                                OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    result?.let { m ->
                                        val uriList: MutableList<Uri> = mutableListOf()
                                        m.forEach { localMedia ->
                                            uriList.add(Uri.fromFile(File(localMedia.realPath)))
                                        }
                                        valueCallback?.onReceiveValue(uriList.toTypedArray())
                                    }
                                }

                                override fun onCancel() {
                                    valueCallback?.onReceiveValue(arrayOf())
                                }
                            })
                        }
                        "audio/*" -> {
                            VideoOrImageUtil.openImageOrVideo(this@BaseWebView.context, object :
                                OnResultCallbackListener<LocalMedia> {
                                override fun onResult(result: MutableList<LocalMedia>?) {
                                    result?.let { m ->
                                        val uriList: MutableList<Uri> = mutableListOf()
                                        m.forEach { localMedia ->
                                            uriList.add(Uri.fromFile(File(localMedia.realPath)))
                                        }
                                        valueCallback?.onReceiveValue(uriList.toTypedArray())
                                    }
                                }

                                override fun onCancel() {
                                    valueCallback?.onReceiveValue(arrayOf())
                                }
                            })
                        }
                        else -> {

                        }
                    }
                    return true
                }
                return super.onShowFileChooser(p0, valueCallback, fileChooserParams)
            }

            override fun onRequestFocus(view: WebView?) {
                super.onRequestFocus(view)
                // 请求此 WebView 的焦点和显示
            }

            /**
             * 进入全屏模式
             */
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                // 进入全屏模式
                Log.d(TAG, "onShowCustomView: 进入全屏模式")
                view?.let {
                    this@BaseWebView.webCallback?.onShowCustomView(it)
                }
            }

            /**
             * 退出全屏模式
             */
            override fun onHideCustomView() {
                super.onHideCustomView()
                // 进入全屏模式
                Log.d(TAG, "onShowCustomView: 退出全屏模式")
                this@BaseWebView.webCallback?.onHideCustomView()
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
                Log.d(TAG, "shouldOverrideUrlLoading: $p1")
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            override fun shouldOverrideUrlLoading(p0: WebView?, p1: WebResourceRequest?): Boolean {
                // super 调用 shouldOverrideUrlLoading(WebView, String):Boolean
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            // 加载资源
            // 网页请求数据
            // 网页的 post 等网络请求操作
            override fun shouldInterceptRequest(
                webView: WebView?,
                url: String?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(webView, url)
            }

            override fun shouldInterceptRequest(
                webView: WebView?,
                webResourceRequest: WebResourceRequest?
            ): WebResourceResponse? {
                Log.d(TAG, "shouldInterceptRequest: ${Thread.currentThread().name}")
                Log.d(TAG, "shouldInterceptRequest: ${webResourceRequest?.url}")
                Log.d(TAG, "shouldInterceptRequest: ${webResourceRequest?.method}")
//                if (webResourceRequest?.url?.equals("http://10.100.144.30:5000") == true) {
                val uri = webResourceRequest?.url
                val strUrl = uri?.toString() ?: ""
//                if (strUrl.startsWith("http://192.168.31.71:8081")

                if (webResourceRequest?.method?.equals("POST") == true) {
                    // post  请求
                    // 符合白名单起始
                    if (strUrl.startsWith("file://${"/zip"}")) {
                        // 更换链接
                        // file:///zip -> http://10.100.144.5000/zip

                        val data = OkHttpUtil.instance.doSyncPost(
//                        strUrl.replace("http://192.168.31.71:8081","http://192.168.31.71:5000"),
                            strUrl.replace(
                                "file:///zip",
                                "http://10.100.144.30:5000/zip"
                            ),
                            hashMapOf(),
                            String::class.java
                        )
                        val webResponseData = WebResourceResponse(
                            "*/*",
                            "utf-8",
                            ByteArrayInputStream(data?.toByteArray())
                        )
                        return webResponseData
                    } else if (strUrl.startsWith("smk://${"/zip"}")) {
                        // 更换链接
                        // smk:///zip -> http://10.100.144.5000/zip

                        val data = OkHttpUtil.instance.doSyncPost(
//                        strUrl.replace("http://192.168.31.71:8081","http://192.168.31.71:5000"),
                            strUrl.replace(
                                "smk:///zip",
                                "http://10.100.144.30:5000/zip"
                            ),
                            hashMapOf(),
                            String::class.java
                        )

                        val webResponseData = WebResourceResponse(
                            "*/*",
                            "utf-8",
                            ByteArrayInputStream(data?.toByteArray())
                        )
                        return webResponseData
                    } else {
                        return super.shouldInterceptRequest(webView, webResourceRequest)
                    }
                } else {
                    return super.shouldInterceptRequest(webView, webResourceRequest)
                }
            }

            override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
                super.onPageStarted(p0, p1, p2)

                // 页面加载完成之后, 注入js
                loadJsFromLocal("test.js")
                loadVConsole()
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                // native
//                loadJsFromLocal("jssdk-web-1.0.0.js")
                // web
                loadJsFromLocal("jssdk-native-1.0.0.js")
                evaluateJavascript("javascript:window.jsbridge.ready()") {}
                this@BaseWebView.webCallback?.onPageFinish()
            }

            override fun onLoadResource(p0: WebView?, p1: String?) {
                super.onLoadResource(p0, p1)
            }

        }
    }

    fun crosPost(webResourceRequest: WebResourceRequest): WebResourceResponse? {
        val uri = webResourceRequest?.url
        val strUrl = uri?.toString() ?: ""
        if (strUrl.startsWith("http://10.100.144.30:8081")
            && (webResourceRequest?.method?.contains("OPTIONS") == true ||
                    webResourceRequest?.method?.contains("POST") == true)
        ) {
            if (webResourceRequest?.method?.contains("OPTIONS") == true) {

                return WebResourceResponse(
                    "text/plain",
                    "utf-8",
                    ByteArrayInputStream("".toByteArray())
                )
            }
            val data = OkHttpUtil.instance.doSyncPost(
//                        strUrl.replace("http://192.168.31.71:8081","http://192.168.31.71:5000"),
                strUrl.replace("http://10.100.144.30:8081", "http://10.100.144.30:5000"),
                hashMapOf(),
                String::class.java
            )
            Log.d(TAG, "shouldInterceptRequest: ${data.toString()}")
            return if (data == null) {
                null
            } else {
                val webResponseData = WebResourceResponse(
                    "*/*",
                    "utf-8",
                    ByteArrayInputStream(data.toByteArray())
                )
                val header = mutableMapOf<String, String>()
                webResourceRequest?.requestHeaders?.let {
                    it.forEach { entry ->
                        header[entry.key] = entry.value
                    }
                }
                webResponseData
//                        header["Access-Control-Allow-Origin"] = "*"
//                        header["Access-Control-Allow-Origin"] = "http://192.168.31.71:8081"
//                        header["Content-Type"] = "text/html;application/json;charset=utf-8"
//                        header["Access-Control-Allow-Credentials"] = "true"
//                        header["Access-Control-Allow-Methods"] = "'POST' , 'OPTIONS'"

                webResponseData.responseHeaders = header
                return webResponseData
            }
        } else {
            return null
        }
    }

    //</editor-fold>


}