package com.lqk.web.ui.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lqk.web.R
import com.lqk.web.ToastUtil
import com.lqk.web.bean.User
import com.lqk.web.callback.CallJsInterface
import com.lqk.web.common.JsBean
import com.lqk.web.common.JsResultBean
import com.lqk.web.common.NativeToJs
import com.lqk.web.databinding.ActivityMainBinding
import com.lqk.web.ui.widget.web.WebActivity
import com.lqk.web.utils.VideoOrImageUtil
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {


    companion object {
        private val TAG = "MainActivity"
        const val NATIVE_TOKEN = "NATIVE_TOKEN"
    }

    private var webView: WebView? = null
    private lateinit var callJS: CallJsInterface<JsResultBean>

    //js文本
    private var wholeJS = ""
    var scrH = 0

    private lateinit var viewBinding: ActivityMainBinding

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置没有 title
        // 普通 Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // AppCompatActivity
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)


        viewBinding = ActivityMainBinding.inflate(layoutInflater)

//        setContentView(R.layout.activity_main)
        setContentView(viewBinding.root)

        webView = WebView(this)
        // web 滑动进度监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView?.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                run {
                    Log.d(TAG, "onCreate: $scrollY")
                    this.scrH = scrollY
                }
            }
        }
        // 添加 webView 对象
        findViewById<FrameLayout>(R.id.fl_web).addView(webView)
//        webView = findViewById(R.id.web_view)
        val url = "file:///android_asset/index.html"
//        val url = "http://10.100.157.175:8080/Gradle___iMTalker___iMTalker_1_0_war/"
        webView!!.settings.defaultTextEncodingName = "utf-8"
        // 设置交互权限 设置 WebView 支持 JavaScript
        webView!!.settings.javaScriptEnabled = true
        // 允许 JS 弹窗
        webView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        val customWebChromeClient = CustomWebChromeClient()
        val customWebViewClient = CustomWebViewClient()
        webView!!.webChromeClient = customWebChromeClient
        webView!!.webViewClient = customWebViewClient
//        webView!!.loadDataWithBaseURL("file:///android_asset/", url, "text/html", "utf-8", null)
        //webView添加读取的js
//        webView!!.loadUrl("javascript:$wholeJS")
        // 挂载对象
        callJS = NativeToJs(this)
//        webView!!.addJavascriptInterface(this, "main")
        webView!!.addJavascriptInterface(callJS, "android")
        webView!!.addJavascriptInterface(callJS, "native")
        webView!!.addJavascriptInterface(callJS, "hfiCore")
        webView!!.addJavascriptInterface(callJS, "__core")
        webView!!.loadUrl(url)
        doCallJs()
        WebView.setWebContentsDebuggingEnabled(true)

    }

    fun base64(str: String): String {
        return Base64.encodeToString(str.toString().toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    fun sha1(str: String): String {
        val messageDigest = MessageDigest.getInstance("SHA")
        val cipher = messageDigest.digest(str.encodeToByteArray())
        val cipherStr = StringBuffer()
        for (i in cipher.indices) {
            val arr = cipher[i].toInt() and 0xff
            if (arr < 16) {
                cipherStr.append("0")
            }
            cipherStr.append(String.format("%02x", arr))
        }
        return cipherStr.toString()
    }

    fun doCallJs() {
        val jsonObj = JSONObject()
        jsonObj.put("msgType", "event")
        jsonObj.put("callbackId", "")
        jsonObj.put("eventName", "onLimitedMode")
        jsonObj.put("params", null)
        webView!!.evaluateJavascript("javascript:window.JSBridge.handleMessage('${base64(jsonObj.toString())}')") {
            Log.d(TAG, "sendInfoToJS: $it")
        }
    }

    fun sendInfoToJS(view: View) {
        WebActivity.start(
            this,
            "https://app.xjbsefjz.fun:11180/sdk-demo/#/"
//            "file:///android_asset/index.html"
        )
//        val msg = (findViewById<View>(R.id.et_android) as EditText).text.toString()
//        Handler().post {
////            StatusBarUtil.setColor(this, Color.parseColor("#00ff00"))
//            if (msg == "login") {
//                return@post
//            } else if (msg == "zxing") {
//                startActivity(Intent(this@MainActivity, ZXingActivity::class.java))
//                return@post
//            }
//            val func = "kotlinToast"
//            val user = User()
//            user.name = "LQK\n"
//            user.age = 22
//            user.obj = mutableListOf("12", "34", "56", "78\n")
//            val str = Gson().toJson(user)
//            val s = JSONObject(str)
//            webView!!.evaluateJavascript("javascript:$func('$s')") {
//                Log.d(TAG, "sendInfoToJS: $it")
//            }
//        }
    }

    fun open(view: View) {
        ToastUtil.toast?.show()
//        startActivity(Intent(this, LocalPackageActivity::class.java))
    }

    private fun logString(vararg str: String) {
        val msg = str.iterator()
        while (msg.hasNext()) {
            Log.d("当前", msg.next())
        }
    }

    @JavascriptInterface
    fun showInfoFormJs(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        //        Log.d(TAG, "showInfoFromJs: " + msg);
    }

    fun reload(view: android.view.View) {
        webView?.clearCache(true)
        webView?.reload()
    }

    inner class CustomWebChromeClient : WebChromeClient() {
        /**
         * 对话框
         */
        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
//            Log.d("Web", "onJsAlert")
            val clazzName1 = Throwable().stackTrace[0].className
            val clazzName2 = Throwable().stackTrace[0].methodName
//            logString(clazzName1, clazzName2, message!!)
            return super.onJsAlert(view, url, message, result)
        }

        /**
         * 确认框
         */
        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
//            Log.d("Web", "onJsConfirm")
            val clazzName1 = Throwable().stackTrace[0].className
            val clazzName2 = Throwable().stackTrace[0].methodName
//            logString(clazzName1, clazzName2)
            return super.onJsConfirm(view, url, message, result)
        }

        /**
         * 输入框
         */
        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
//            Log.d("Web", "onJsPrompt")
            if (url?.startsWith(NATIVE_TOKEN) ?: return false) {

            }
            if (message?.contains("js") == true) {
//                Log.d("Web", "$message")
                val jsBean = JsBean()
                jsBean.functionName = "toast"
                val user = User()
                user.name = "LQK"
                user.age = 22
                jsBean.data = Gson().toJson(user)
                val str = Gson().toJson(jsBean)
//                result?.confirm(str)
                val clazzName1 = Throwable().stackTrace[0].className
                val clazzName2 = Throwable().stackTrace[0].methodName
//                logString(clazzName1, clazzName2)
                return true
            }
            return super.onJsPrompt(view, url, message, defaultValue, result)
        }

        /**
         * Js 卸载之前
         */
        override fun onJsBeforeUnload(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
//            Log.d("Web", "onJsBeforeUnload")
            return super.onJsBeforeUnload(view, url, message, result)
        }

        // 网页打印信息
        override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
//            Toast.makeText(this@MainActivity, "${p0?.message()}", Toast.LENGTH_SHORT)
//                .show()
            return true
        }

        override fun onShowFileChooser(
            webView: WebView?,
            valueCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            // h5 <input type="file"> 选择图片
            fileChooserParams?.let {
                val acceptType = it.acceptTypes
                val title = it.title
                val mode = it.mode
                when (acceptType[0]) {
                    "image/*" -> {
                        VideoOrImageUtil.openImage(this@MainActivity, object :
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
                        VideoOrImageUtil.openVideo(this@MainActivity, object :
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
                        VideoOrImageUtil.openImageOrVideo(this@MainActivity, object :
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
            }
            return true
        }
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(p0: WebView?, p1: String?) {
            super.onPageFinished(p0, p1)
            loadJsFromLocal("index", "index.js")
            loadJsFromLocal("login", "login.js")
            loadJsFromLocal("test.js")
        }

        override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: $p1")
            return super.shouldOverrideUrlLoading(p0, p1)
        }

        override fun shouldOverrideUrlLoading(p0: WebView?, p1: WebResourceRequest?): Boolean {
            Log.d(TAG, "shouldOverrideUrlLoading: 2 $p1")
            return super.shouldOverrideUrlLoading(p0, p1)
        }

        private val KEY_FONT = "/android_asset_font/"

        override fun shouldInterceptRequest(
            webView: WebView?,
            url: String?
        ): WebResourceResponse? {
            Log.d(TAG, "shouldInterceptRequest: 1 $url")
            // 字体
            if (url != null && url.contains(KEY_FONT)) {
                try {
                    val assetPath = url.substring(
                        url.indexOf(KEY_FONT) + KEY_FONT.length,
                        url.length
                    )
                    Log.d(TAG, "shouldInterceptRequest: $assetPath")
                    val header = hashMapOf<String, String>()
                    header.put("Access-Control-Allow-Origin", "*")
                    header.put("Access-Control-Allow-Headers", "Content-Type")
                    return WebResourceResponse(
                        "application/octet-stream",
                        "UTF8",
                        200,
                        "OK",
                        header,
//                        this@MainActivity.resources.assets.open(assetPath)
                        this@MainActivity.resources.assets.open("fonts/fzkt.TTF")
                    )
                } catch (e: Exception) {
                    Log.d(TAG, "shouldInterceptRequest: 字体加载异常")
                    e.printStackTrace()
                }
            }
            return super.shouldInterceptRequest(webView, url)
        }

        override fun shouldInterceptRequest(
            p0: WebView?,
            p1: WebResourceRequest
        ): WebResourceResponse? {
            Log.d(TAG, "shouldInterceptRequest: 2 ${p1.url}")
            return super.shouldInterceptRequest(p0, p1)
        }

        override fun shouldInterceptRequest(
            p0: WebView?,
            p1: WebResourceRequest?,
            p2: Bundle?
        ): WebResourceResponse? {
            Log.d(TAG, "shouldInterceptRequest: 3 ${p1?.url}")
            return super.shouldInterceptRequest(p0, p1, p2)
        }
    }

    /**
    javascript:try{
    if(document.head && !document.getElementById('UTEST_injectScript')) {
    varinjectScript = document.createElement('script');
    injectScript.src='file:///sdcard/inject.js';
    injectScript.id='UTEST_injectScript';
    document.head.appendChild(injectScript);
    }
    }catch(e) {}
     */
    private fun loadJsFromLocal(id: String, filePath: String) {
        webView?.evaluateJavascript(
            "javascript:try{" +
                    "if(document.head && !document.getElementById('USER_$id')) {\n" +
                    "var injectScript = document.createElement('script');" +
                    "injectScript.src='file:///android_asset/$filePath';" +
                    "injectScript.id='USER_$id';" +
                    "document.head.appendChild(injectScript);" +
                    "}" +
                    "}catch(e){}"
        ) {
            "".takeUnless {
                true
            }
        }
    }

    private fun loadJsFromLocal(fileName: String) {
        webView?.evaluateJavascript("javascript:${readStringFromJs(fileName)}") {
            Log.d(TAG, "loadJsFromLocal: $it")
        }
    }

    /**
     * 读取 assets 文件夹中的 js文件 转为 String
     * 可以提供给子类复写, 替换相关关键字
     */
    private fun readStringFromJs(fileName: String): String {
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


    fun cg(view: android.view.View) {
        if (webView?.url?.startsWith("file") == true) {
//            webView?.loadUrl("http://10.100.157.175:8080/Gradle___iMTalker___iMTalker_1_0_war/")
            webView?.loadUrl("http://192.168.31.71:8081/estateOwner")
        } else {
            webView?.loadUrl("file:///android_asset/index.html")
        }
    }

    fun openFile(view: android.view.View) {
        // 打开文件
        val params = hashMapOf<String, String>()
        params["style"] = "1"
        params["local"] = "true"
        params["memuData"] = "{}"
        //
        QbSdk.openFileReader(
            this,
            "/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download/Android Framework 开发揭秘.pdf",
            null
        ) { p0 ->
            run {
                Log.d(TAG, "onReceiveValue: $p0")
            }
        }
    }

    fun doJs(view: android.view.View) {
//        val jsStr = "document.getElementsByClassName('name_input3')[0].style.display='none';"
        // 直接执行 一段js: (function(){})()
//        val jsStr = "document.getElementById('name_input3').style.display='none';"
//        webView?.loadUrl("javascript:(function() { $jsStr })()")
        webView?.loadUrl("javascript:hfiJSBridge.onWeb('${viewBinding.etAndroid.text.toString()}')")
    }

    fun doJsEvn(view: android.view.View) {
        webView?.evaluateJavascript(
            "javascript:showInfo('${viewBinding.etAndroid.text.toString()}')",
            object : ValueCallback<String> {
                override fun onReceiveValue(p0: String?) {
                    Log.d(TAG, "onReceiveValue: $p0")
                }
            })
    }

    fun doWebView(view: android.view.View) {
        // html 真实高度 * 缩放比 - WebView 的高度 = 可滚动的高度
        //
        Log.d(TAG, "doWebView: 真实高度:${webView?.contentHeight}")
        Log.d(TAG, "doWebView: 缩放比:${webView?.scale}")
        Log.d(TAG, "doWebView: webView的高度:${webView?.height}")
        val scrollY = ((webView?.contentHeight ?: 0) * (webView?.scale ?: 0f)).toInt()
        Log.d(TAG, "doWebView: webView内容高度:$scrollY")
        val scrollYH =
            (webView?.contentHeight ?: 0) * (webView?.scale ?: 0f) - (webView?.height ?: 0)
        Log.d(TAG, "doWebView: 可滚动的高度:${scrollYH}")
        Log.d(TAG, "doWebView: 已经滚动的高度:${this.scrH}")
        val scrollNum = (webView?.scrollY ?: 0) / scrollY
//        webView?.webViewClient?.
        Log.d(TAG, "doWebView: 比例:${scrollNum}")
        // 滚动的高度 + web基本高度 / 总高度
        val s = (scrH + (webView?.height ?: 0)) / scrollY.toFloat()
        Log.d(TAG, "doWebView: 比例:${String.format("%.4f", s)}")
    }
}
