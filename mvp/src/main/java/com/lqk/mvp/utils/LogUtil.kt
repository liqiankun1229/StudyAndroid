package com.lqk.mvp.utils

import android.util.Log

/**
 * @author LQK
 * @time 2019/5/13 19:01
 * @remark 日志
 */
object LogUtil {
    private var isDebug = true

    /**
     * 是否打开调试开关
     * true  -> debug
     * false -> release
     */
    fun initDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    /**
     * 打印调试日志
     */
    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    /**
     * 打印警告日志
     */
    fun w(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    fun i() {

    }

    fun v() {

    }

    fun e() {

    }

    fun wtf() {

    }
}