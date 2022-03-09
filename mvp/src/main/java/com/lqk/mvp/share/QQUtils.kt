package com.lqk.mvp.share

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lqk.mvp.BuildConfig
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent

/**
 * @author LQK
 * @date 2021/10/21
 * @remark
 */
object QQUtils {
    private const val APP_ID = "1106894700"
    private const val APP_KEY = "OLltLMBYAKV55K7P"
    private var tencent: Tencent? = null

    fun register(application: Application) {
        tencent = Tencent.createInstance(APP_ID, application, "${BuildConfig.APPLICATION_ID}.fileProvider")

        Tencent.setIsPermissionGranted(true, APP_KEY)
    }

    fun shareText(context: Activity, summary: String, listener: IUiListener) {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, summary)
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com")
        tencent?.shareToQQ(context, params, listener)
    }
}