package com.lqk.web.common

import com.google.gson.Gson

/**
 * @author LQK
 * @time 2019/1/16 15:43
 * @remark
 */
class JsBean : JsResultBean() {
    // 方法名
    var functionName = ""
    // 数据 ->  json 格式的字符串
    var data = ""

    companion object {
        fun checkParameter(jsBean: JsBean): Boolean {
            return try {
                var bean = Gson().fromJson(jsBean.data, JsBean::class.java)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}