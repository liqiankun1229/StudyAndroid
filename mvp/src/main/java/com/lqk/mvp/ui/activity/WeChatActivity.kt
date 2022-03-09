package com.lqk.mvp.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import com.lqk.mvp.common.Constants
import com.lqk.mvp.utils.DeviceUtil
import com.lqk.mvp.utils.FileUtil
import com.lqk.mvp.wxapi.WXUtils
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * @author lqk
 * @date 2018/7/10
 * @time 8:38
 * @remarks
 */
@Route(path = "/mvp/register")
class WeChatActivity : BaseActivity() {

    private val btnShareMessage = 0x1001
    private val btnOpenWX = 0x1002
    private val etUserName = 0x1003
    private val btnOpenSCX = 0x1004
    private val btnOpenLog = 0x1005
    private val btnShareMiniProgram = 0x1006
    private val btnShareImage = 0x1007
    private val btnShareMedia = 0x1008
    private val btnShareVideo = 0x1009
    private val btnShareWeb = 0x1010
    private val btnIpAddress = 0x1011
    private val cbWechat = 0x1012

    private lateinit var api: IWXAPI


    override fun layoutId(): Int {
        return -1
    }

    override fun initView() {
        // 界面 布局
        verticalLayout {
            padding = dip(30)
            backgroundColor = Color.WHITE
            editText {
                background = getDrawable(R.drawable.bg_elevation_bottom)
                id = etUserName
                textColor = Color.BLACK
                hint = "用户名"
                textSize = 24f
                top = 10
                bottom = 10
            }.lparams {
                topMargin = dip(16)
                bottomMargin = dip(16)
                width = matchParent
            }
            checkBox {
                id = cbWechat
                textColor = Color.BLACK
                text = "分享到朋友圈"
            }
            button {
                text = "分享文字"
                id = btnShareMessage
            }
            button {
                text = "打开微信"
                id = btnOpenWX
            }
            button {
                text = "打开小程序主页"
                id = btnOpenSCX
            }
            button {
                text = "分享图片"
                id = btnShareImage
            }
            button {
                text = "分享音频"
                id = btnShareMedia
            }
            button {
                text = "分享视频"
                id = btnShareVideo
            }
            button {
                text = "分享网页"
                id = btnShareWeb
            }
            button {
                text = "分享小程序"
                id = btnShareMiniProgram
            }
            button {
                text = "获取本地IP"
                id = btnIpAddress
            }
        }

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false)
        //将APP_ID注册到微信中
        api.registerApp(Constants.WX_APP_ID)

        find<Button>(btnShareMessage).setOnClickListener {
            var shareText = find<EditText>(etUserName).text.toString()
            if (shareText == "") {
                shareText = "微信文字分享"
            }
            onClickSendMsgText(shareText ?: "微信文字分享")
        }

        find<Button>(btnOpenWX).setOnClickListener {
            alert {
                customTitle {
                    verticalLayout {
                        textView {
                            gravity = View.TEXT_ALIGNMENT_CENTER
                            text = "打开微信"
                        }
                    }
                }
                okButton {
                    if (!api.isWXAppInstalled) {
                        Toast.makeText(this@WeChatActivity, "没有安装微信", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this@WeChatActivity, "${api.openWXApp()}", Toast.LENGTH_SHORT)
                        .show()
                }
                cancelButton {
                    finish()
                }
            }.show()
        }
        find<Button>(btnOpenSCX).setOnClickListener {
            val req = WXLaunchMiniProgram.Req()
            req.userName = Constants.WX_XCX_ID
//            req.path = "" // path 路径可以拼装 参数 实现带参传递
            req.path = "pages/index/index" // 不配置，默认主页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW
            api.sendReq(req)
        }
        find<Button>(btnShareImage).setOnClickListener {
            WXUtils.shareImage(
                this,
                FileUtil.bitmapToByteArray(this, R.drawable.picture),
                if (findViewById<CheckBox>(cbWechat).isChecked) {
                    WXUtils.Scene.Session
                } else {
                    WXUtils.Scene.TimeLine
                }
            )
        }
        find<Button>(btnShareMedia).setOnClickListener {
            WXUtils.shareAudio(
                "https://y.qq.com/n/yqq/song/0010e3Cd0kZt9S.html",
                "https://y.qq.com/n/yqq/song/0010e3Cd0kZt9S.html",
                "https://lqk-im-talker.oss-cn-shanghai.aliyuncs.com/%E5%87%BA%E5%B1%B1.mp3",
                "https://lqk-im-talker.oss-cn-shanghai.aliyuncs.com/%E5%87%BA%E5%B1%B1.mp3",
                "出山",
                "演唱-小倩",
                FileUtil.bitmapToByteArray(this, R.drawable.picture),
                if (findViewById<CheckBox>(cbWechat).isChecked) {
                    WXUtils.Scene.Session
                } else {
                    WXUtils.Scene.TimeLine
                }
            )
        }
        find<Button>(btnShareVideo).setOnClickListener {
            //https://dss1.bdstatic.com/5eN1bjq8AAUYm2zgoY3K/r/www/cache/scholar_mid/static/protocol/https/xueshu/img/scholar_logo_e95073e.png
            Glide.with(this).asBitmap()
            WXUtils.shareVideo(
                "https://y.qq.com/n/yqq/song/0010e3Cd0kZt9S.html",
                "https://y.qq.com/n/yqq/song/0010e3Cd0kZt9S.html",
                "视频",
                "搞笑",
                FileUtil.bitmapToByteArray(this, R.drawable.picture),
                if (findViewById<CheckBox>(cbWechat).isChecked) {
                    WXUtils.Scene.Session
                } else {
                    WXUtils.Scene.TimeLine
                }
            )
        }
        find<Button>(btnShareWeb).setOnClickListener {

            WXUtils.shareWeb(
                "https://www.baidu.com",
                "百度首页", "搜索",
                FileUtil.bitmapToByteArray(this, R.drawable.picture),
                if (findViewById<CheckBox>(cbWechat).isChecked) {
                    WXUtils.Scene.Session
                } else {
                    WXUtils.Scene.TimeLine
                }
            )
        }
        find<Button>(btnShareMiniProgram).setOnClickListener {
            WXUtils.shareMiniProgram(
                "测试", "分享的小程序信息", FileUtil.bitmapToByteArray(this, R.drawable.picture),
                "https://www.baidu.com", Constants.WX_XCX_ID, "pages/index/index",
                WXUtils.MiniType.PREVIEW, withShareTicket = false
            )
        }
        find<Button>(btnIpAddress).setOnClickListener {
            Toast.makeText(
                this,
                "${DeviceUtil.getIPAddress(applicationContext)}",
                Toast.LENGTH_SHORT
            ).show()
//            setAirPlaneMode(airPlaneMode)
//            airPlaneMode = !airPlaneMode
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private var airPlaneMode = false

    private fun bitToByteArray(): ByteArray {
        val res = resources
        val bitmap = BitmapFactory.decodeResource(res, R.drawable.picture)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    //给当前文本加上时间
    private fun stringAddTime(text: String?): String {
        return if (text == null) System.currentTimeMillis()
            .toString() else text + System.currentTimeMillis()
    }

    private fun onClickSendMsgText(msg: String) {
        WXUtils.shareMessage(msg, if (findViewById<CheckBox>(cbWechat).isChecked) {
            WXUtils.Scene.Session
        } else {
            WXUtils.Scene.TimeLine
        })
//        val text = msg
//        if (text.isEmpty()) {
//            return
//        }
//        //1.创建一个用于封装待分享的WXTextObject对象
//        val textObj = WXTextObject()
//        textObj.text = text
//
//        //2.创建一个WXMediaMessage对象，该对象用于android客户端像微信发送数据
//        val msg = WXMediaMessage()
//        msg.mediaObject = textObj
//        msg.description = text
//
//        //3.创建一个用于请求微信客户端的SendMessageToWX.Req对象
//        val req = SendMessageToWX.Req()
//        req.message = msg
//        //设置请求唯一标识：加上当前时间
//        req.transaction = stringAddTime(text)
//        //选择发送给朋友 还是朋友圈
//        req.scene = SendMessageToWX.Req.WXSceneSession//发送给朋友
//        //发送给朋友圈
////        req.scene = SendMessageToWX.Req.WXSceneTimeline
//
//        //4.发送给微信客户端 api.sendReq(req)
//        api.sendReq(req)
////        Toast.makeText(this@RegisterActivity, "${api.sendReq(req)}", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0x101 -> {
//                val appdata = WXAppExtendObject()
//                val path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/")
//                appdata.filePath = path
//                appdata.extInfo = "this is ext info"
//
//                val msg = WXMediaMessage()
//                msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true))
//                msg.title = "this is title"
//                msg.description = "this is description"
//                msg.mediaObject = appdata

                //1.创建一个用于封装待分享的WXTextObject对象
//                val textObj = WXTextObject()
//                textObj.text = "text"
//
//                //2.创建一个WXMediaMessage对象，该对象用于android客户端像微信发送数据
//                val msg = WXMediaMessage()
//                msg.mediaObject = textObj
//                msg.description = "消息内容"
//
//                val req = SendMessageToWX.Req()
//                req.transaction = stringAddTime("text")
//                req.message = msg
//                req.scene = SendMessageToWX.Req.WXSceneTimeline
//                api.sendReq(req)
//
//                finish()
            }
        }
    }

//   private fun setAirPlaneMode(boolean: Boolean) {
//        val mode = if (boolean) {
//            1
//        } else {
//            0
//        }
//        val strCMD = "setting put global airplane_mode_on $mode"
//        try {
//            Runtime.getRuntime().exec(strCMD)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    private fun setAirPlaneMode(boolean: Boolean) {
        val mode = if (boolean) {
            1
        } else {
            0
        }
        val strCMD = "setting put global airplane_mode_on $mode"
        try {
            Settings.Global.putInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, mode)
//            Settings.System.putString(contentResolver, Settings.System.AIRPLANE_MODE_ON, mode)
            val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            intent.putExtra("state", boolean)
            sendBroadcast(intent)
//            Runtime.getRuntime().exec(strCMD)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}