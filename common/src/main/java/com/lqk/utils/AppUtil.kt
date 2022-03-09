package com.lqk.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * @author LQK
 * @time 2018/12/22 13:52
 * @remark APP 级工具类
 */
object AppUtil {

  fun appIsInstall(packageName: String) {

  }

  /**
   * 检测app是否运行
   */
  fun appIsLauncher(context: Context, packageName: String): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processInfos = activityManager.runningAppProcesses
    val appPid = Process.myPid()
    for (processInfo in processInfos) {
      if (packageName == processInfo.processName && appPid == processInfo.pid) {
        return true
      }
    }
    return false
  }

  fun isTopActivity(packageName: String, context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val componentName = activityManager.getRunningTasks(1)[0].topActivity
    val currentPackageName = componentName?.packageName
    return (currentPackageName != null && currentPackageName == packageName)
  }

}