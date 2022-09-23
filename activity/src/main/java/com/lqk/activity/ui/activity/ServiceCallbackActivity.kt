package com.lqk.activity.ui.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import com.lqk.activity.R
import com.lqk.activity.databinding.ActivityServiceCallbackBinding
import com.lqk.activity.service.CallbackService
import com.lqk.base.BaseVBActivity

@SuppressLint("HandlerLeak")
class ServiceCallbackActivity : BaseVBActivity<ActivityServiceCallbackBinding>(), ServiceConnection, View.OnClickListener {

    companion object {
        const val TAG = "ServiceCallback"
    }

    private var mIntent: Intent? = null
    private var mServiceBinder: CallbackService.MyBinder? = null

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    val mainHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg != null) {
                viewBinding.tvData.text = msg.data!!.getString("data")
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mServiceBinder = service!! as CallbackService.MyBinder
        mServiceBinder!!.getService().setCallBack(object : CallbackService.DataCallBack {
            override fun onDataStringChange(data: String) {
                Log.d(TAG, "回调")
                viewBinding.tvData.text = "回调成功"
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_bind_service -> {
                if (mServiceBinder != null) {
                    // 回调执行 函数
                    mServiceBinder!!.setMyData("Call Service")
                } else {
                    bindService(mIntent, this, Context.BIND_AUTO_CREATE)
                    Toast.makeText(this, "Binder is null", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_get_service_data -> {
                if (mServiceBinder != null) {
                    // 回调执行 函数
                    mServiceBinder!!.getDataCallback()

                }
            }
            R.id.btn_update_thread -> {
                Log.d(TAG, "开始更新")
                val thread = object : Thread() {
                    override fun run() {
                        super.run()
//                        tv_data.text = "子线程更新"
                        val message = Message()
                        val bundle = Bundle()
                        bundle.putString("data", "子线程更新")
                        message.data = bundle
                        mainHandler.sendMessage(message)
                    }
                }
                thread.start()
            }
            R.id.btn_update_post -> {
                Thread(Runnable {
                    viewBinding.tvData.post {
                        viewBinding.tvData.text = "Post 更新数据"
                    }
                }).start()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_service_callback
    }

    override fun initViewBinding(): ActivityServiceCallbackBinding {
        return ActivityServiceCallbackBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        mIntent = Intent(this, CallbackService::class.java)
    }
}
