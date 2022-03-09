package com.example.dev

/**
 * @author LQK
 * @date 2021/12/7 9:20
 * @remark 全局异常捕获
 */
class AppExceptionHandler : Thread.UncaughtExceptionHandler {

    companion object{
        private const val TAG = "AppExceptionHandler"
    }

    override fun uncaughtException(t: Thread, e: Throwable) {

    }
}