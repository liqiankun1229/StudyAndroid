package com.lqk.web.ui.widget.web.client

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * @author LQK
 * @time 2022/1/13 15:22
 * @remark
 */
class CustomWebViewClient:WebViewClient() {
    /**
     * 重载 URL
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    /**
     * 重载 URL
     */
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    /**
     * 页面开始加载
     */
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    /**
     * 页面加载结束
     * 注入js
     */
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    /**
     * 加载资源
     */
    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
    }

    /**
     * 请求拦截
     */
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        // 数据缓存
        return null
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }


}