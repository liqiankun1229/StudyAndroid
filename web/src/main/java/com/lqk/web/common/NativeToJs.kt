package com.lqk.web.common

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import com.lqk.web.application.WebApplication
import com.lqk.web.bean.User
import com.lqk.web.callback.CallJsInterface
import com.tencent.smtt.sdk.WebView

/**
 * @author LQK
 * @time 2019/1/16 10:21
 * @remark Js 方法注入
 */
open class NativeToJs<T : JsResultBean>(private val context: Context) : CallJsInterface<T> {

    /**
     * Js 调用本地原生方法
     */
    @JavascriptInterface
    fun onJsToNativeFuncation(t: T) {

    }

    companion object {
        private const val TAG = "LoginToJS"
    }

    @JavascriptInterface
    override fun jsCallNative(t: T) {
        Toast.makeText(this.context, "$t", Toast.LENGTH_SHORT).show()
    }

    /**
     * JS 调用原生方法 无参
     */
    @JavascriptInterface
    override fun onNative() {

    }

    @JavascriptInterface
    fun toastFromJs(msg: String) {
        if (context is Activity) {
            Toast.makeText(
                context, "Kotlin我是JS传过来的：${msg}", Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                WebApplication.getApplication(), "Kotlin我是JS传过来的：${msg}", Toast.LENGTH_SHORT
            ).show()
        }
    }

//    /**
//     * JS 调用原生方法 有参
//     */
//    @JavascriptInterface
//    override fun onNative(jsBean: JsBean) {
//        val user = Gson().fromJson(jsBean.data, User::class.java)
//        if (context is Activity) {
//            Toast.makeText(
//                context,
//                "Kotlin我是JS传过来的：${jsBean.functionName}:${user.name}:${user.age}",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            Toast.makeText(
//                WebApplication.getApplication(),
//                "Kotlin我是JS传过来的：${jsBean.functionName}:${user.name}:${user.age}",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    /**
     * JS 调用原生方法 有参
     */
    @JavascriptInterface
    override fun onNative(response: String) {
        Log.d(TAG, "onNative: $response")
//        val jsBean = Gson().fromJson(response, JsBean::class.java)
//        val user = Gson().fromJson(jsBean.data, User::class.java)
        if (context is Activity) {
            Toast.makeText(
                context,
                "Kotlin我是JS传过来的：${response}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                WebApplication.getApplication(),
                "Kotlin我是JS传过来的：${response}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun toastDialog(webView: WebView?, msg: String) {
        Handler().post {
            webView?.loadUrl("javascript:showInfoFormJava('$msg')")
//            webView!!.loadUrl("javascript:showAlert()")
        }
    }

    /**
     * 调用js方法
     * @param func 方法名称
     * @param data 入参- JSON 格式
     */
    override fun nativeCallJS(webView: WebView?, func: String, data: String) {
        Thread {
            webView?.loadUrl("javascript:$func('$data')")
        }.start()
    }

    @JavascriptInterface
    override fun toastJs(msg: String) {
        try {
            val jsBean = Gson().fromJson(msg, JsBean::class.java)
            val user = Gson().fromJson(jsBean.data, User::class.java)

            if (context is Activity) {
                Toast.makeText(
                    context,
                    "Kotlin我是JS传过来的：${jsBean.functionName}:${user.name}:${user.age}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    WebApplication.getApplication(),
                    "Kotlin我是JS传过来的：${jsBean.functionName}:${user.name}:${user.age}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context, "Kotlin我是JS传过来的：$msg", Toast.LENGTH_SHORT
            ).show()
        }

    }

    @JavascriptInterface
    fun showInfoFormJs(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}