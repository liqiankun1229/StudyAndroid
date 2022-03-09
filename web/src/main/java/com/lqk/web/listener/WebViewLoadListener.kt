package com.lqk.web.listener

import android.graphics.Bitmap
import android.webkit.WebView

/**
 * @author LQK
 * @time 2019/1/21 22:26
 * @remark 网页加载监听
 */
interface WebViewLoadListener {
    /**
     * 开始加载
     */
    fun onWebPageStarted(webView: WebView, url: String, favicon: Bitmap)

    /**
     * Web 加载完毕
     */
    fun onWebPageFinished(webView: WebView, url: String)

    /**
     * 加载进度
     */
    fun onProgressChanged(webView: WebView, progress: Int)

    /**
     * 获取 HTML 标题
     */
    fun onReceivedTitle(webView: WebView, title: String)
}