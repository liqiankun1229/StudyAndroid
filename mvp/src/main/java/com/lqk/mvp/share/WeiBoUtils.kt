package com.lqk.mvp.share

import android.content.Context
import com.sina.weibo.sdk.api.*
import com.sina.weibo.sdk.auth.AuthInfo
import java.util.*

/**
 * @author LQK
 * @date 2021/10/22
 * @remark
 * 微博分享
 *
 */
object WeiBoUtils {

    private const val APP_KEY = "887466000"
    /*
   Apple ID： 1506896350
   Bundle ID：com.cztv.kanyuhangNew
   Android包名：com.hfi.yuhang
   Android签名：2d0a535d9cfde063add4d6c23a20b2b7
   App Secret：1af1af8a42d7733d1a7ac2d90f7726c4
     */

    fun initWeiBo(context: Context) {
        val authInfo = AuthInfo(context, APP_KEY, "", "")
    }

    // 分享文字
    fun shareTxt(txt: String) {
        val message = WeiboMultiMessage()
        val textObject = TextObject()
        textObject.text = txt
        message.textObject = textObject
    }

    // 分享图片
    fun sharePicture(file: String) {
        val message = WeiboMultiMessage()
        val imageObject = ImageObject()
        imageObject.setImagePath(file)
        message.imageObject = imageObject
    }

    // 分享网页
    fun shareWebPage(url: String) {
        val message = WeiboMultiMessage()
        val webpageObject = WebpageObject()
        webpageObject.identify = UUID.randomUUID().toString()
        webpageObject.title = "标题"
        webpageObject.description = "描述"

        webpageObject.actionUrl = url
        webpageObject.defaultText = "分享的网页"
        message.mediaObject = webpageObject
    }
    // 分享图片 - 多图

    // 分享视频
    fun shareVideo(){
        val message = WeiboMultiMessage()
        val videoSourceObject = VideoSourceObject()
        message.videoSourceObject = videoSourceObject
    }
    // 分享到超话
}