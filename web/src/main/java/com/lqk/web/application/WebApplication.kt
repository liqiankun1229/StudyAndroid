package com.lqk.web.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.lqk.web.log.ErrorHandle
import com.lqk.web.utils.MMKVUtils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk


/**
 * @author LQK
 * @time 2019/1/23 21:14
 * @remark
 */
class WebApplication : Application() {


    companion object {

        private lateinit var instance: Application

        fun getApplication(): Application {
            return instance
        }
    }

    var topActivity: Activity? = null

    private val mRegisterLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {

            Log.d("lifecycle", "onActivityCreated: ${activity.localClassName}")
        }

        override fun onActivityStarted(activity: Activity) {

            Log.d("lifecycle", "onActivityStarted: ${activity.localClassName}")
        }

        override fun onActivityResumed(activity: Activity) {
            // 重新赋值 正在显示的 Activity
            (WebApplication.getApplication() as WebApplication).topActivity = activity
            Log.d("lifecycle", "onActivityResumed: ${activity.localClassName}")
        }

        override fun onActivityPaused(activity: Activity) {

            Log.d("lifecycle", "onActivityPaused: ${activity.localClassName}")
        }

        override fun onActivityStopped(activity: Activity) {

            Log.d("lifecycle", "onActivityStopped: ${activity.localClassName}")
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

            Log.d("lifecycle", "onActivitySaveInstanceState: ${activity.localClassName}")
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d("lifecycle", "onActivityDestroyed: ${activity.localClassName}")
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.d("start_time", "attachBaseContext: ${System.currentTimeMillis()}")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 全局异常
        Thread {
            ErrorHandle.getInstance().initError(this)
        }.start()

        // 生命周期
        registerActivityLifecycleCallbacks(mRegisterLifecycleCallbacks)

        // mmkv
        MMKVUtils.initMMKV(this)
        // 初始化
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        // 文档能力
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }
        })
        if (Build.VERSION.SDK_INT >= 29 && QbSdk.getTbsVersion(this) < 45114) {
            Log.d("Application", "onCreate: ${Build.VERSION.SDK_INT}")
            Log.d("Application", "onCreate: ${QbSdk.VERSION}")

            QbSdk.forceSysWebView()
        }
    }
}