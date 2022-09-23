package com.lqk.web.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import com.tencent.smtt.sdk.WebView

/**
 * @author LQK
 * @time 2019/1/26 23:29
 * @remark 网页 WebView 封装
 */
class WebContainerView : WebView {

    constructor(context: Context) : super(context) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWebView()
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, privateBrowsing: Boolean)
            : super(context, attrs, defStyleAttr, privateBrowsing) {
        initWebView()
    }

    /**
     * 初始化 WebView 配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        // 5.0 以上，开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }
        this.settings.loadWithOverviewMode = true
        this.settings.useWideViewPort = true
        // 允许 js 代码
        /// Android 4.3 调用reload 会回调多次 WebChromeClient.onJsPrompt()
        this.settings.javaScriptEnabled = true
        removeJavaScriptInterfaces(this)
        // 允许 SessionStorage/ LocalStorage 存储
        this.settings.domStorageEnabled = true
        // 缩放
        this.settings.displayZoomControls = false
        this.settings.builtInZoomControls = false
        // 文字缩放
        this.settings.textZoom = 100
        // 缓存 10M API 18之后 ，系统自动处理
        this.settings.setAppCacheMaxSize(10 * 1024 * 1024)
        // 缓存
        this.settings.setAppCacheEnabled(true)
        // 缓存地址
        this.settings.setAppCachePath(context.getDir("appcache", 0).path)
        // 文件File 协议
        this.settings.allowFileAccess = true
        // 保存密码
        this.settings.savePassword = false
        // 设置 UA
        this.settings.userAgentString = "${this.settings.userAgentString} kaolaApp/${context.packageName}"
        // 移除部分系统 JavaScript 接口

        // 自动加载 图片
        this.settings.loadsImagesAutomatically = true

    }

    fun loadHtmlOfUrl(url: String) {
        this.loadUrl(url)
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
}
