package com.lqk.mvp.wxapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lqk.mvp.R
import com.lqk.mvp.utils.FileUtil
import com.lqk.mvp.utils.TimeUtil
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * @author lqk
 * @date 2018/8/1
 * @time 14:33
 * @remarks
 */
object WXUtils {

    private const val ACTION_REFRESH_WXAPP = "ACTION_REFRESH_WXAPP"
    private var api: IWXAPI? = null

    private const val THUMB_SIZE = 100


    // 分享到
    enum class Scene {
        Session, // 对话
        TimeLine, // 朋友圈
        Favorite // 收藏
    }

    // 小程序类型
    enum class MiniType {
        RELEASE, // 正式版
        TEST, // 测试版
        PREVIEW // 预览版
    }


    /**
     * 注册到微信
     */
    fun registerToWeChat(context: Context, APP_ID: String, debug: Boolean = false) {
        api = WXAPIFactory.createWXAPI(context, APP_ID, debug)
//        api?.registerApp(APP_ID)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                api?.registerApp(APP_ID)
            }
        }, IntentFilter(ACTION_REFRESH_WXAPP))
    }

    /**
     * 分享文字信息
     */
    fun shareMessage(msg: String, scene: Scene) {
        if (api != null) {
            // 1.创建一个用于封装待分享的WXTextObject对象
            val wxTextObject = WXTextObject()
            wxTextObject.text = msg
            // 2.创建一个WXMediaMessage对象，该对象用于android客户端像微信发送数据
            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxTextObject
            wxMediaMessage.description = msg
            // 3.创建一个用于请求微信客户端的SendMessageToWX.Req对象
            val sendMessageToWX = SendMessageToWX.Req()
            // 设置请求唯一标识：加上当前时间
            sendMessageToWX.transaction = TimeUtil.addStringTime(msg)
            sendMessageToWX.message = wxMediaMessage
            // 发送对象 1.朋友 2.朋友圈 3.收藏
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            api?.sendReq(sendMessageToWX)
        }
    }

    /**
     * 分享图片信息
     */
    fun shareImage(context: Context, img: ByteArray, scene: Scene) {
        if (api != null) {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.picture)

            // 初始化 WXImageObject
            val wxImageObject = WXImageObject()
            wxImageObject.imageData = img

            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxImageObject

            // 设置缩略图
            val thumb = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true)
            bitmap.recycle()
            wxMediaMessage.thumbData = FileUtil.bitmapToByteArray(thumb)

            val sendMessageToWX = SendMessageToWX.Req()
            sendMessageToWX.transaction = TimeUtil.addStringTime("image")
            sendMessageToWX.message = wxMediaMessage
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            sendMessageToWX.userOpenId = ""
            api?.sendReq(sendMessageToWX)
        }
    }

    /**
     * 分享音频信息
     */
    fun shareAudio(musicUrl: String, musicLowBandUrl: String, musicDataUrl: String, musicLowBandDataUrl: String,
                   title: String, description: String, thumb: ByteArray, scene: Scene) {
        if (api != null) {
            val wxMusicObject = WXMusicObject()
            wxMusicObject.musicUrl = musicUrl
            wxMusicObject.musicLowBandUrl = musicLowBandUrl
            wxMusicObject.musicDataUrl = musicDataUrl
            wxMusicObject.musicLowBandDataUrl = musicLowBandDataUrl

            // 分享后好友显示信息
            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxMusicObject
            wxMediaMessage.title = title
            wxMediaMessage.description = description
            wxMediaMessage.thumbData = thumb

            val sendMessageToWX = SendMessageToWX.Req()
            sendMessageToWX.transaction = TimeUtil.addStringTime("music")
            sendMessageToWX.message = wxMediaMessage
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            sendMessageToWX.userOpenId = ""
            api?.sendReq(sendMessageToWX)
        }
    }

    /**
     * 分享视频信息
     */
    fun shareVideo(videoUrl: String, videoLowBandUrl: String, title: String, description: String, thumb: ByteArray, scene: Scene) {
        if (api != null) {
            val wxVideoObject = WXVideoObject()
            wxVideoObject.videoUrl = videoUrl
            wxVideoObject.videoLowBandUrl = videoLowBandUrl

            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxVideoObject
            wxMediaMessage.title = title
            wxMediaMessage.description = description
            wxMediaMessage.thumbData = thumb

            val sendMessageToWX = SendMessageToWX.Req()
            sendMessageToWX.transaction = TimeUtil.addStringTime("video")
            sendMessageToWX.message = wxMediaMessage
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            sendMessageToWX.userOpenId = ""
            api?.sendReq(sendMessageToWX)

        }
    }

    /**
     * 分享网页信息
     */
    fun shareWeb(webPageUrl: String, title: String, description: String, thumb: ByteArray, scene: Scene) {
        if (api != null) {
            val wxWebPageObject = WXWebpageObject()
            wxWebPageObject.webpageUrl = webPageUrl

            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxWebPageObject
            wxMediaMessage.title = title
            wxMediaMessage.description = description
            wxMediaMessage.thumbData = thumb

            val sendMessageToWX = SendMessageToWX.Req()
            sendMessageToWX.transaction = TimeUtil.addStringTime("webPage")
            sendMessageToWX.message = wxMediaMessage
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            sendMessageToWX.userOpenId = ""
            api?.sendReq(sendMessageToWX)
        }
    }

    /**
     * 分享小程序
     */
    fun shareMiniProgram(title: String, msg: String, thumb: ByteArray,
                         url: String, miniProgramId: String, path: String,
                         miniProgramType: MiniType, scene: Scene = WXUtils.Scene.Favorite, withShareTicket: Boolean = false) {
        if (api != null) {
            val wxMiniProgramObject = WXMiniProgramObject()
            wxMiniProgramObject.webpageUrl = url
            wxMiniProgramObject.withShareTicket = withShareTicket
            wxMiniProgramObject.miniprogramType = when (miniProgramType) {
                WXUtils.MiniType.RELEASE -> {
                    WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
                }
                WXUtils.MiniType.TEST -> {
                    WXMiniProgramObject.MINIPROGRAM_TYPE_TEST
                }
                WXUtils.MiniType.PREVIEW -> {
                    WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
                }
            }
            wxMiniProgramObject.userName = miniProgramId
            wxMiniProgramObject.path = path

            val wxMediaMessage = WXMediaMessage()
            wxMediaMessage.mediaObject = wxMiniProgramObject
            wxMediaMessage.title = title
            wxMediaMessage.description = msg
            wxMediaMessage.thumbData = thumb

            val sendMessageToWX = SendMessageToWX.Req()
            sendMessageToWX.transaction = ""
            sendMessageToWX.message = wxMediaMessage
            sendMessageToWX.scene = when (scene) {
                WXUtils.Scene.Session -> {
                    SendMessageToWX.Req.WXSceneSession
                }
                WXUtils.Scene.TimeLine -> {
                    SendMessageToWX.Req.WXSceneTimeline
                }
                WXUtils.Scene.Favorite -> {
                    SendMessageToWX.Req.WXSceneFavorite
                }
            }
            api?.sendReq(sendMessageToWX)
        }
    }


}