package com.lqk.web.callback

import android.webkit.JavascriptInterface
import com.lqk.web.common.JsResultBean
import com.tencent.smtt.sdk.WebView

/**
 * @author LQK
 * @time 2019/1/16 14:18
 * @remark
 */
interface CallJsInterface<T : JsResultBean> {
    @JavascriptInterface
    fun onNative()

    @JavascriptInterface
    fun onNative(response: String)

//    @JavascriptInterface
//    fun onNative(jsBean: JsBean)

    /**
     * 在网页弹出对话框
     */
    fun toastDialog(webView: WebView?, msg: String)

    /**
     * 调用 Html JS 的方法
     */
    @JavascriptInterface
    fun nativeCallJS(webView: WebView?, func: String, data: String)

    @JavascriptInterface
    fun jsCallNative(t: T)

    @JavascriptInterface
    fun toastJs(msg: String)
}