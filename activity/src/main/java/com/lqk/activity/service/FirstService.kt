package com.lqk.activity.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.lqk.activity.utils.DataUtil


/**
 * @date 2018/9/16
 * @time 0:13
 * @remarks
 */
@SuppressLint("Registered")
class FirstService : Service() {


    private var mBinder: MyBinder = MyBinder()

    companion object {
        const val TAG = "FirstService"
        var mData2 = "执行任务中。。。"
    }

    var mData = "正在 执行服务"

    override fun onCreate() {
        Log.d(TAG, "----- onCreate 创建 -----")
        super.onCreate()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.d(TAG, "----- onStart -----")
        super.onStart(intent, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "----- onBind -----")
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "----- onRebind ------")
        super.onRebind(intent)
    }

    class MyBinder : Binder() {
        fun setMyData(data: String) {
            FirstService.mData2 = data
            DataUtil.name = data
            Log.d(TAG, data)
        }

        fun startService() {
            Log.d(TAG, "开始服务")
        }

        fun getServiceInt(): Int {
            return 0
        }

        fun getData2(): String {
            return FirstService.mData2
        }

        fun getServiceString(): String {
            return "服了个五"
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            mData2 = intent.getStringExtra("data")?:""
            Log.d(TAG, mData2)
        }
        Log.d(TAG, "----- onStartCommand -----")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "---- onDestroy() 停止销毁服务 ----")
    }
}