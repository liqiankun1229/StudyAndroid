package com.lqk.activity.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log


/**
 * @date 2018/9/17
 * @time 11:15
 * @remarks
 */
object ActivityStatusUtil {

    const val TAG = "ActivityStatusUtil"


    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    private fun isAppAlive(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = activityManager.runningAppProcesses
        for (info in processInfos) {
            if (info.processName == packageName) {
                Log.i("NotificationLaunch", "the $packageName is running, isAppAlive return true")
                return true
            }
        }
        Log.i("NotificationLaunch", "the $packageName is not running, isAppAlive return false")
        return false
    }

    /**
     * 判断MainActivity是否活动
     * @param context 一个context
     * @param activityName 要判断Activity
     * @return boolean
     */
    private fun isMainActivityAlive(context: Context, activityName: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(100)
        for (info in list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.toString() == activityName || info.baseActivity.toString() == activityName) {
                Log.i(TAG, info.topActivity?.packageName + " info.baseActivity.getPackageName()=${info.baseActivity?.packageName}")
                return true
            }
        }
        return false
    }

    /**
     * 检测某Activity是否在当前Task的栈顶
     */
    private fun isTopActivity(context: Context, activityName: String): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfos = manager.getRunningTasks(1)
        var cmpNameTemp: String? = null
        if (runningTaskInfos != null) {
            cmpNameTemp = runningTaskInfos[0].topActivity.toString()
        }
        return if (cmpNameTemp == null) {
            false
        } else cmpNameTemp == activityName
    }

}