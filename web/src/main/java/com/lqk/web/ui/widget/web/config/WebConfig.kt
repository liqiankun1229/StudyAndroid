package com.lqk.web.ui.widget.web.config

/**
 * @author LQK
 * @time 2022/1/18 10:48
 * @remark
 */
object WebConfig {

    // 字段
    const val KEY_FUNC_NAME = "funcName"
    const val KEY_PARAMS = "params"
    const val KEY_CALLBACK = "callbackName"
    const val KEY_ERR_CODE = "errCode"
    const val KEY_ERR_MSG = "errMsg"

    // 方法字段, 由原生维护
    // string 名称
    val funcList = hashMapOf<String, Any>(
        "brightness" to 1,
    )

}