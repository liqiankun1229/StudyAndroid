package com.lqk.mvp.http

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


/**
 * @author LQK
 * @time 2022/6/25 22:34
 *
 */
object LogUtils {
    fun initLogger(showLog: Boolean = false) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.t("Http")
    }

    fun d(msg: String) {
        Logger.d(msg)
    }

    fun i(msg: String) {
        Logger.i(msg)
    }

    fun w(msg: String, e: Throwable) {
        Logger.w("$msg: ${e.message}")
    }

    fun e(msg: String, e: Throwable) {
        Logger.e(e, msg)
    }

    fun json(json: String) {
        Logger.json(json)
    }
}