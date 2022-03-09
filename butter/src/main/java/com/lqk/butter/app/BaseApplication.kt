package com.lqk.butter.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author LQK
 * @time 2019/2/27 10:44
 * @remark
 */
class BaseApplication : Application() {

//    private lateinit var instance: BaseApplication

    private var isDebug = true

    companion object {

        // 维护一个activity Stack
        private var activityStack: MutableList<Activity> = mutableListOf()

        private lateinit var instance: BaseApplication

        fun getInstance(): BaseApplication {
            return instance
        }
    }

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    fun clearActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
        System.exit(0)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    fun checkIcon(oldComponentName: ComponentName, newComponentName: ComponentName) {
        if (oldComponentName.className != newComponentName.className) {
            val mPm = packageManager
            mPm.setComponentEnabledSetting(oldComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
//            mPm.setComponentEnabledSetting(newComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

            mPm.setComponentEnabledSetting(ComponentName(this, newComponentName.className), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0)
            reStartLauncher()
        }
    }

    fun reStartLauncher() {
        val mPm = packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val resolvers = mPm.queryIntentActivities(intent, 0)
        for (resolver in resolvers) {
            if (resolver.activityInfo != null) {
                val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                am.killBackgroundProcesses(resolver.activityInfo.packageName)
            }
        }
    }
}