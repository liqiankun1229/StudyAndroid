package com.lqk.mvp.app

import android.app.Application
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.lqk.mvp.common.Constants
import com.lqk.mvp.http.LogUtils
import com.lqk.mvp.share.QQUtils
import com.lqk.mvp.wxapi.WXUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
//import me.jessyan.autosize.AutoSizeConfig
//import me.jessyan.autosize.unit.Subunits

/**
 * @author lqk
 * @date 2018/8/8
 * @time 9:58
 * @remarks
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        // 屏幕适配
//        AutoSizeConfig.getInstance().getUnitsManager()
//            .setSupportDP(false)
//            .setSupportSP(false)
//            .setSupportSubunits(Subunits.PT)
        MultiDex.install(this)
        // 路由
        ARouter.openLog() // 开启日志打印
        ARouter.openDebug() // 开启调试
        ARouter.init(this)
        // 注册微信工具
        WXUtils.registerToWeChat(this, APP_ID = Constants.WX_APP_ID)
        // QQ
        QQUtils.register(this)
//        DDUtils.initDD(this)
        // Flutter 初始化
//        FlutterMain.startInitialization(this)

        Logger.addLogAdapter(AndroidLogAdapter())
        LogUtils.initLogger(true)
    }
}