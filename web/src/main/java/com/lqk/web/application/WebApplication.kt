package com.lqk.web.application

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()
        instance = this
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
    }
}