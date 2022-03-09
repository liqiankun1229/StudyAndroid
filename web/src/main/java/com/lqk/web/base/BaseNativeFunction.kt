package com.lqk.web.base

import android.content.Context
import android.webkit.JavascriptInterface

/**
 * @date 2018/9/9
 * @time 0:02
 * @remarks
 */
open class BaseNativeFunction constructor(context: Context) {

    companion object {
        const val TAG = "BaseNativeFunction"
    }

    @JavascriptInterface
    fun onNativeFunction(func: String, args: Any) {

    }

    fun onJavaScriptFunction() {

    }


}