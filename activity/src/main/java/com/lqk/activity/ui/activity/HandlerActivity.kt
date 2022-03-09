package com.lqk.activity.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.R

@SuppressLint("HandlerLeak")
class HandlerActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "HandlerActivity"
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v.id) {
            R.id.btn_send_handler -> {
                object : Thread() {
                    override fun run() {
                        super.run()
                        Log.d(TAG, "子线程发送消息 -- sendMessage")
                        val message = Message()
                        message.what = 0
                        message.obj = "消息内容"
                        mainHandle.sendMessage(message)
                    }
                }.start()
            }
            R.id.btn_post_handler -> {
                mainHandle.post {
                    Log.d(TAG, "发送")
                }
//                object :Thread(){
//                    override fun run() {
//                        super.run()
//                        Log.d(TAG, "子线程发送消息 -- post")
//                        val message = Message()
//                        message.what = 0
//                        message.obj = "消息内容"
//                        mainHandle.post(object :Runnable{
//                            override fun run() {
//
//                            }
//                        })
//                    }
//                }
            }
        }
    }

    private var mainHandle: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg != null) {
                when (msg.what) {
                    0 -> {
                        Log.d(TAG, "处理 --- ${msg.obj as String} ---")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)


    }
}
