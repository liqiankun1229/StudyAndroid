package com.example.dev

import android.content.Context
import android.util.Log

/**
 * @author LQK
 * @date 2021/10/1
 * @remark
 */
class AppException private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "AppException"

        val instance: AppException by lazy {
            AppException()
        }
    }

    private var context: Context? = null
    private var mDefaultException: Thread.UncaughtExceptionHandler? = null

    fun initException(context: Context) {
        this.context = context
        // 默认捕获器
        mDefaultException = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        Log.d(TAG, "uncaughtException: ${p1.message}")
    }
}