package com.lqk.mvp.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lqk.mvp.R
import com.lqk.mvp.common.Constants
import com.lqk.mvp.ui.activity.WeChatActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_wx_share.*

/**
 * 微信操作回调
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    companion object {
        private val TIMELINE_SUPPORTED_VERSION = 0x21020001
    }

    private var api: IWXAPI? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_wx_share)
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false)
        // 接收到的 intent 传递 handleIntent 方法，处理结果
        api!!.handleIntent(intent, this)
        share.setOnClickListener {
            shareText("测试微信分享功能", 0)
        }
        share2.setOnClickListener {
            shareText("测试微信分享功能", 1)
        }
        result.setOnClickListener {
            startActivity(Intent(this, WeChatActivity::class.java))
        }
        goHome()
    }

    private fun goHome() {
        startActivity(Intent(this, WeChatActivity::class.java))
        finish()
    }

    private fun shareText(msg: String, type: Int) {
        // msg 消息内容 type 分享类型
        val wxTextObject = WXTextObject()
        wxTextObject.text = msg
        val wxMediaMessage = WXMediaMessage()
        wxMediaMessage.mediaObject = wxTextObject
        wxMediaMessage.description = msg
        //
        val req = SendMessageToWX.Req()
        req.transaction = stringAddTime(msg)
        req.message = wxMediaMessage
        req.scene = type
        api!!.sendReq(req)
    }

    //给当前文本加上时间
    private fun stringAddTime(text: String?): String {
        return if (text == null) System.currentTimeMillis()
            .toString() else text + System.currentTimeMillis()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        when (req.type) {
            ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX -> {
                goToGetMsg()
            }
            ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX -> {
                goToShowMsg(req as ShowMessageFromWX.Req)
            }
            else -> {
            }
        }
    }

    override fun onResp(resp: BaseResp) {
        when (resp.type) {
            ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> {
                // 小程序返回
                val launcherPro = resp as WXLaunchMiniProgram.Resp
                val msg = launcherPro.extMsg
                Toast.makeText(applicationContext, "小程序返回：$msg", Toast.LENGTH_LONG).show()
            }
            else -> {
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> {
                        result.text = "分享成功"
                    }
                    BaseResp.ErrCode.ERR_USER_CANCEL -> {
                        result.text = "分享取消"
                    }
                    BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                        result.text = "分享拒绝"
                    }
                    BaseResp.ErrCode.ERR_UNSUPPORT -> {
                        result.text = "分享"
                    }
                    BaseResp.ErrCode.ERR_BAN -> {
                        result.text = "分享失败"
                    }
                    BaseResp.ErrCode.ERR_SENT_FAILED -> {
                        result.text = "发送失败"
                    }
                    else -> {
                        result.text = "分享失败"
                    }
                }
//                goHome()
                Toast.makeText(
                    applicationContext,
                    "${result.text}:${resp.errCode}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                val intent = Intent(this@WXEntryActivity, MainActivity::class.java)
//                intent.putExtras(getIntent())
//                startActivity(intent)
//                finish()
//            }
//        }, 3000)
    }

    private fun goToGetMsg() {

    }

    private fun goToShowMsg(showReq: ShowMessageFromWX.Req) {
        Log.d("WX", "${showReq.message}")
        //		WXMediaMessage wxMsg = showReq.message;
        //		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
        //
        //		StringBuffer msg = new StringBuffer();
        //		msg.append("description: ");
        //		msg.append(wxMsg.description);
        //		msg.append("\n");
        //		msg.append("extInfo: ");
        //		msg.append(obj.extInfo);
        //		msg.append("\n");
        //		msg.append("filePath: ");
        //		msg.append(obj.filePath);
        //
        //		Intent intent = new Intent(this, ShowFromWXActivity.class);
        //		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
        //		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
        //		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
        //		startActivity(intent);
        //		finish();
    }
}