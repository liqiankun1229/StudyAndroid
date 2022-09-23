package com.lqk.rxjava

import android.app.Application
import android.content.Context

/**
 * @author LQK
 * @time 2022/8/11 23:40
 *
 */
class RxJavaApplication:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
    }
}