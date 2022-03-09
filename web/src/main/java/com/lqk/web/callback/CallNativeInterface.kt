package com.lqk.web.callback

import android.webkit.JavascriptInterface
import com.lqk.web.common.JsBean

/**
 * @author LQK
 * @time 2019/1/21 22:14
 * @remark
 */
interface CallNativeInterface {

    @JavascriptInterface
    fun onNative()

    @JavascriptInterface
    fun onNative(jsBean: JsBean)

    @JavascriptInterface
    fun onNative(jsBean: String)
}