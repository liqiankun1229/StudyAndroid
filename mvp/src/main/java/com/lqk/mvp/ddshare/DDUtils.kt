package com.lqk.mvp.ddshare

import android.content.Context
import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory
import com.android.dingtalk.share.ddsharemodule.IDDShareApi
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage
import com.android.dingtalk.share.ddsharemodule.message.DDTextMessage
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD

/**
 * @author LQK
 * @date 2021/10/21
 * @remark
 */
object DDUtils {
    const val APP_ID = "1334881629"
    private const val APP_KEY = "dinggb3ksmjwmovxyh6b"
    private const val APP_SECRET =
        "RlX1Uh3fVufQVsYLUJknvPPXpoqmwdyrrU_USgE1wSYwzPJAbs-apSA6YZhtmF0Z"
    private var iddShareApi: IDDShareApi? = null
    fun initDD(context: Context):DDUtils {
        iddShareApi = DDShareApiFactory.createDDShareApi(context, APP_KEY, false)
        return this
    }

    /**
     * 是否安装了钉钉
     */
    fun isDDInstalled(): Boolean {
        return iddShareApi?.isDDAppInstalled == true
    }

    /**
     * 是否支持分享到钉钉
     */
    fun isDDShare(): Boolean {
        return iddShareApi?.isDDSupportAPI == true
    }

    fun isDDShareToDing(): Boolean {
        return iddShareApi?.isDDSupportDingAPI == true
    }

    /**
     * 分享文字消息
     */
    fun shareTxt(text: String, isSendDing: Boolean = false) {
//        if (iddShareApi)
        //
        val textObject = DDTextMessage()
        textObject.mText = text
        //
        val mediaMessage = DDMediaMessage()
        mediaMessage.mMediaObject = textObject
        //
        val req = SendMessageToDD.Req()
        req.mMediaMessage = mediaMessage
        // 调用APi接口发送信息到钉钉
        if (isSendDing) {
            iddShareApi?.sendReqToDing(req)
        } else {
            iddShareApi?.sendReq(req)
        }
    }

    // 分线网页消息

    // 分享图片 - 二进制流方式

    // 分享图片 - 图片链接方式

    // 分享图片 - 本地图片
}