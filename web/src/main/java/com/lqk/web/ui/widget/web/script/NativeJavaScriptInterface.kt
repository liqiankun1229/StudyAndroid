package com.lqk.web.ui.widget.web.script

import android.util.Log
import android.webkit.JavascriptInterface
import com.lqk.web.BuildConfig
import com.lqk.web.ui.widget.web.WebCallBack
import com.lqk.web.ui.widget.web.config.WebConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


/**
 * @author LQK
 * @time 2022/1/14 9:54
 * @remark 默认提供 Native 和 H5 交互 -> H5 调用原生
 * 回调时 要持有 h5
 */
open class NativeJavaScriptInterface(
    private val webCallBack: WebCallBack?,
    private val callback: OnJsNeedCallBack
) {
    companion object {
        private const val TAG = "JavaScriptInterface"
    }

    /**
     * 接受 H5 数据, 进行解析分发
     */
    @OptIn(DelicateCoroutinesApi::class)
    @JavascriptInterface
    fun onNative(param: String) {
        try {
            Log.d(TAG, "onNative: $param")
            val jsonObj = JSONObject(param)
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onNative:funName: ${jsonObj[WebConfig.KEY_FUNC_NAME]}")
                Log.d(TAG, "onNative:params: ${jsonObj[WebConfig.KEY_PARAMS]}")
                // 回调方法名称 是一段表示 唯一标记的字符串
                Log.d(TAG, "onNative:callbackName: ${jsonObj[WebConfig.KEY_CALLBACK]}")
            }
            // 安全判断
            WebConfig.funcList[WebConfig.KEY_FUNC_NAME]
            // todo 非 用户操作相关 这里处理直接返回结果
            if (WebConfig.funcList.isEmpty()) {
                callback.onFailed(500, "未找到相关实现", jsonObj[WebConfig.KEY_CALLBACK].toString())
            } else {
                // 调用相关方法 todo 切换到主线程
                GlobalScope.launch(Dispatchers.Main) {
                    onNative(
                        funcName = jsonObj.get(WebConfig.KEY_FUNC_NAME).toString(),
                        params = LinkedHashMap(),
                        callback = callback,
                        jsonObj[WebConfig.KEY_CALLBACK].toString()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 分发
     */
    private fun onNative(
        funcName: String,
        params: LinkedHashMap<String, Any>,
        callback: OnJsNeedCallBack? = null,
        callbackName: String
    ) {
        // todo Web 调用原生方法
        webCallBack?.h5callNative(funcName, params, callback, callbackName)
    }

    /**
     * 事件回调接口
     */
    interface OnJsNeedCallBack {
        /**
         * 返回 成功后的参数
         */
        fun onSucceed(params: LinkedHashMap<String, Any>, callbackName: String)

        /**
         * 返回失败 code 和 msg
         */
        fun onFailed(code: Int, msg: String, callbackName: String)
    }
}