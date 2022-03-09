package com.lqk.activity.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log


/**
 * @date 2018/9/16
 * @time 0:13
 * @remarks
 */
@SuppressLint("Registered")
class CallbackService : Service() {

    private lateinit var mDataCallBack: DataCallBack

    private var mBinder: CallbackService.MyBinder = MyBinder()

    /**
     * 相当于类静态变量
     */
    companion object {
        const val TAG = "FirstService"
        var mData2 = "执行任务中。。。"
    }

    var mData = "正在 执行服务"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "创建")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return mBinder
    }

    /**
     * 内部类关键字 inner
     */
    inner class MyBinder : Binder() {
        fun setMyData(data: String) {
            this@CallbackService.mData = data
            Log.d(TAG, data)
        }

        fun getMyData(): String {
            return this@CallbackService.mData
        }

        fun getDataCallback() {
            this@CallbackService.mDataCallBack.onDataStringChange(this@CallbackService.mData)
        }

        fun getService(): CallbackService {
            return this@CallbackService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            mData2 = intent.getStringExtra("data") ?: ""
            Log.d(TAG, mData2)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun setCallBack(call: DataCallBack) {
        this.mDataCallBack = call
    }

    fun getCallBack(): DataCallBack {
        return this.mDataCallBack
    }

    interface DataCallBack {
        fun onDataStringChange(data: String)
    }

}