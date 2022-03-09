package com.lqk.web.ui.widget.web.client

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.lqk.web.ui.widget.web.WebCallBack

/**
 * @author LQK
 * @time 2022/1/13 15:17
 * @remark
 */
class CustomWebChromeClient(var context: Context, var callback: WebCallBack?) : WebChromeClient() {

    // 标题
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        title?.let {
            callback?.onTitle(it)
        }
    }
}