package com.lqk.web.ui.widget.web

import com.google.gson.Gson

/**
 * @author LQK
 * @time 2022/1/24 15:44
 * @remark
 */
class WebBean {
    var errCode: Int = 0
    var errMsg: String = ""
    var params: LinkedHashMap<String, Any>? = null
    var callbackName: String = ""

    companion object {
        /**
         * @param code 默认 0
         * @param msg 默认成功
         * @param callbackName 回调时对应的函数
         * @param params 成功时,返回的参数
         */
        fun create(
            code: Int = 0,
            msg: String = "成功",
            callbackName: String,
            params: LinkedHashMap<String, Any>? = null
        ): WebBean {
            val webBean = WebBean()
            webBean.errCode = code
            webBean.errMsg = msg
            webBean.callbackName = callbackName
            webBean.params = params
            return webBean
        }
    }
}

fun WebBean.toJson(): String {
    return Gson().toJson(this)
}