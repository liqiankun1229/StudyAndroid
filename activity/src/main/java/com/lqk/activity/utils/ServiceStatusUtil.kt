package com.lqk.activity.utils

import android.app.ActivityManager
import android.app.Service
import android.content.Context

/**
 * @date 2018/9/17
 * @time 10:54
 * @remarks
 */
object ServiceStatusUtil {

    fun getServiceStatus() {}

    fun isServiceRunning(context: Context, serviceName: String): Boolean {
        // 校验 服务是否存在
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(100)

        for (info in services) {
            // 得到 所有正在运行的服务 的名称
            val name = info.service.className
            if (serviceName == name) {
                return true
            }
        }
        return false
    }

    fun isServiceRunning(context: Context, service: Service): Boolean {
        val servicePath = service.packageName
        // 校验 服务是否存在
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(100)

        for (info in services) {
            // 得到 所有正在运行的服务 的名称
            val name = info.service.className
            if (service.packageName == name) {
                return true
            }
        }
        return false
    }
}