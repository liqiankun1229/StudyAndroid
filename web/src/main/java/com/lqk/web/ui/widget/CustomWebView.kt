package com.lqk.web.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


// android.webkit.ConsoleMessage     -> com.tencent.smtt.export.external.interfaces.ConsoleMessage
// android.webkit.CacheManager       -> com.tencent.smtt.sdk.CacheManager(deprecated)
// android.webkit.CookieManager      -> com.tencent.smtt.sdk.CookieManager
// android.webkit.CookieSyncManager	 -> com.tencent.smtt.sdk.CookieSyncManager
// android.webkit.CustomViewCallback -> com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback
// android.webkit.DownloadListener   -> com.tencent.smtt.sdk.DownloadListener
// android.webkit.GeolocationPermissions -> com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback
// android.webkit.HttpAuthHandler    -> com.tencent.smtt.export.external.interfaces.HttpAuthHandler
// android.webkit.JsPromptResult     -> com.tencent.smtt.export.external.interfaces.JsPromptResult
// android.webkit.JsResult           -> om.tencent.smtt.export.external.interfaces.JsResult
// android.webkit.SslErrorHandler    -> com.tencent.smtt.export.external.interfaces.SslErrorHandler
// android.webkit.ValueCallback      -> com.tencent.smtt.sdk.ValueCallback
// android.webkit.WebBackForwardList -> com.tencent.smtt.sdk.WebBackForwardList
// android.webkit.WebChromeClient    -> com.tencent.smtt.sdk.WebChromeClient
// android.webkit.WebHistoryItem     -> com.tencent.smtt.sdk.WebHistoryItem
// android.webkit.WebIconDatabase    -> com.tencent.smtt.sdk.WebIconDatabase
// android.webkit.WebResourceResponse -> com.tencent.smtt.export.external.interfaces.WebResourceResponse
// android.webkit.WebSettings        -> com.tencent.smtt.sdk.WebSettings
// android.webkit.WebSettings.LayoutAlgorithm -> com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm
// android.webkit.WebStorage         -> com.tencent.smtt.sdk.WebStorage
// android.webkit.WebView            -> com.tencent.smtt.sdk.WebView 控件
// android.webkit.WebViewClient      -> com.tencent.smtt.sdk.WebViewClient 客户端


/**
 * @author LQK
 * @time 2021/10/29
 * @remark WebView 腾讯 TBS
 * 加载网页 http://   https://
 * 支持 js -> 继承BaseJSInterface
 * 支持 native -> H5 javescript
 * 支持 回调
 * 设置 UA
 * 页面加载进度监听
 *
 *
 * 优化: 启动优化
 *
 */
abstract class CustomWebView : WebView {

    companion object {
        const val TAG = "CustomWebView"
    }

    constructor(context: Context?, p1: Boolean) : super(context, p1)
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, p1: AttributeSet?) : this(context, p1, -1)
    constructor(context: Context?, p1: AttributeSet?, p2: Int) : this(context, p1, p2, false)
    constructor(context: Context?, p1: AttributeSet?, p2: Int, p3: Boolean) : this(
        context,
        p1,
        p2,
        null,
        p3
    )

    constructor(
        p0: Context?,
        p1: AttributeSet?,
        p2: Int,
        p3: MutableMap<String, Any>?,
        p4: Boolean
    ) : super(p0, p1, p2, p3, p4) {
        initWeb()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb() {
        // 5.0+ 开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        // 允许 JS 代码
        settings.javaScriptEnabled = true
        // 允许 SessionStorage / LocalStorage 存储
        settings.domStorageEnabled = true
        // 禁用缩放
        settings.displayZoomControls = false
        settings.builtInZoomControls = false
        // 禁用文字缩放
        settings.textZoom = 100
        // 缓存
        settings.setAppCacheMaxSize(10 * 1024 * 1024L)
        // 允许缓存
        settings.setAppCacheEnabled(true)
        // 缓存位置
        settings.setAppCachePath(context.getDir("webCache", 0).path)
        // 允许 WebView 使用 File 协议
        settings.allowFileAccess = true
        // 保存密码 true:允许 false:禁止
        settings.savePassword = false
        // 设置UA
        settings.userAgentString = uaSetting()
        // 移除部分 JavaScript 接口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
            && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1
        ) {
            removeJavascriptInterface("searchBoxJavaBridge_")
            removeJavascriptInterface("accessibility")
            removeJavascriptInterface("accessibilityTraversal")
        }
        // 自动加载图片
        settings.loadsImagesAutomatically = true

        // 用来实现标题定制、加载进度条控制、jsBridge交互、url拦截、错误处理（包括http、资源、网络）
        // WebViewClient
        webViewClient = object : WebViewClient() {
            // 返回 true:链接由客户端处理, WebView 不做操作; false:客户端处理 该url
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            // Web 资源加载
            override fun shouldInterceptRequest(p0: WebView?, p1: String?): WebResourceResponse {
                return super.shouldInterceptRequest(p0, p1)
            }
        }
        // WebChromeClient
        webChromeClient = object : WebChromeClient() {
            // alert 方法
            override fun onJsAlert(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
                return super.onJsAlert(p0, p1, p2, p3)
            }

            // 5.0+ <input> 标签
            override fun onShowFileChooser(
                p0: WebView?,
                p1: ValueCallback<Array<Uri>>?,
                p2: FileChooserParams?
            ): Boolean {
                return super.onShowFileChooser(p0, p1, p2)
            }

            // <input> 标签
            override fun openFileChooser(p0: ValueCallback<Uri>?, p1: String?, p2: String?) {
                super.openFileChooser(p0, p1, p2)
            }
        }
    }

    /**
     * 设置 UA
     */
    abstract fun uaSetting(): String

    private var userTouchUrl: Boolean = false

    /**
     * 重定向
     * 301 永久重定向
     * 302 临时重定向
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                userTouchUrl = true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun loadUrl(url: String?) {
        Log.d(TAG, "loadUrl: $url")
        super.loadUrl("https://www.baidu.com")
//        super.loadUrl(p0)

    }
}