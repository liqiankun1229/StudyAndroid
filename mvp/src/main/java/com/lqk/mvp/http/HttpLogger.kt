package com.lqk.mvp.http

import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author LQK
 * @time 2022/6/25 18:39
 *
 */
class HttpLogger : HttpLoggingInterceptor.Logger {
    private var mMessage = StringBuilder()
    override fun log(message: String) {
        var logMsg = message
        if (logMsg.startsWith("--> POST")) {
            mMessage.setLength(0)
        }
        // json 格式化
        if ((logMsg.startsWith('{') && logMsg.endsWith('}'))
            || (logMsg.startsWith('[') && logMsg.endsWith(']'))
        ) {
            logMsg = JsonUtils.formatJson(JsonUtils.decodeUnicode(logMsg))
        }
        mMessage.append("$logMsg\n")
        if (logMsg.startsWith("<-- END HTTP")) {
            LogUtils.d(mMessage.toString())
        }
    }
}