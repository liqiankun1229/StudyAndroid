package com.lqk.web.log

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.MessageQueue
import android.util.Log

/**
 * @author LQK
 * @time 2022/8/20 11:59
 *
 */
object MsgListenerHandle {
    fun startListener() {
        val idleHandler = MessageQueue.IdleHandler {
            Log.d("MsgListenerHandle", "startListener: ")
            false
        }
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Looper.myQueue().addIdleHandler(idleHandler)
            }
        }
    }
}