package com.lqk.mvp.share

import android.app.Activity
import android.content.Context
import android.util.Log
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.openapi.IWBAPI
import com.sina.weibo.sdk.openapi.SdkListener
import com.sina.weibo.sdk.openapi.WBAPIFactory
import com.sina.weibo.sdk.share.WbShareCallback

/**
 * @author LQK
 * @date 2021/10/22
 * @remark
 */
class WeiboShareUtils private constructor() : WbShareCallback {

    companion object {
        private const val APP_KEY = "3859619957"
        val instance: WeiboShareUtils by lazy { WeiboShareUtils() }
        private var weiboShare: IWBAPI? = null

        fun initWeibo(context: Context): WeiboShareUtils {
            val authInfo = AuthInfo(context, APP_KEY, "", "")
            weiboShare = WBAPIFactory.createWBAPI(context)
            weiboShare?.registerApp(context, authInfo, object : SdkListener {
                override fun onInitSuccess() {

                }

                override fun onInitFailure(p0: Exception?) {

                }
            })
            Thread.sleep(500)
            return instance
        }

        fun api(): IWBAPI? {
            return weiboShare
        }

    }

    fun check(): Boolean {
        return weiboShare?.isWBAppInstalled == true
    }

    fun shareTxt(activity: Activity, txt: String): Boolean {
        return if (weiboShare?.isWBAppInstalled == true) {
            val message = WeiboMultiMessage()
            val textObject = TextObject()
            textObject.text = txt
            message.textObject = textObject
            weiboShare?.shareMessage(activity, message, false)
            true
        } else {
            false
        }
    }

    override fun onComplete() {
        Log.d("WeiboShareUtils", "onComplete: 分享")
    }

    override fun onError(p0: UiError?) {
        Log.d("WeiboShareUtils", "onError: ${p0?.errorMessage}")
    }

    override fun onCancel() {
        Log.d("WeiboShareUtils", "onCancel: 分享")
    }
}