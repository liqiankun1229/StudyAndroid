package com.example.dev

import android.app.Application

/**
 * @author LQK
 * @date 2021/10/1
 * @remark
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 全局 异常捕获
//        AppException.instance.initException(this)
    }
}