package com.lqk.activity.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.view.WindowManager

/**
 * @author lqk
 * @date 2018/5/25
 * @time 19:41
 * @remarks
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        resetDensity()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        Log.d("Application", "resetDensity: ${System.}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        resetDensity()
    }

    fun resetDensity() {
        val size = Point()
        (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)
        resources.displayMetrics.xdpi = size.x / DESIGN_WIDTH * 72f
    }

    companion object {
        // 绘制页面时参照的设计宽度
        const val DESIGN_WIDTH = 1080f
    }
}