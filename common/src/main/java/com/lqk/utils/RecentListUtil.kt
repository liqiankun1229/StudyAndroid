package com.lqk.utils

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log

/**
 * @author LQK
 * @time 2019/9/10 16:11
 * @remark
 */
object RecentListUtil {

  fun getRecentList(context: Context) {
    var activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    var procInfos = activityManager.getRunningTasks(10)
    for (info in procInfos) {
      Log.d("Processes", "${info.baseActivity} + ${info.topActivity}")
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  fun getRecent(context: Context) {
    var activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    var recentTaskList = activityManager.appTasks
    for (recentTask in recentTaskList) {
      Log.d("Processes", "${recentTask.taskInfo.baseActivity?.className}")
      Log.d("Processes", "${recentTask.taskInfo.baseActivity?.shortClassName}")
    }
  }

  fun recentTaskList() {
//        try {
//            var servicesManager = Class.forName("android.os.ServiceManager")
//            var service = servicesManager.getMethod("getService", String::class.java)
//            var reBinder = service.invoke(servicesManager, "statusbar") as IBinder
//            var statusBar = Class.forName(reBinder.interfaceDescriptor)
//            var statusBarObject = statusBar.classes[0].getMethod("asInterface", IBinder::class.java).invoke(null, arrayOf(reBinder))
//            var clearAll = statusBar.getMethod("toggleRecentApps")
//            clearAll.isAccessible = true
//            clearAll.invoke(statusBarObject)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
  }

}