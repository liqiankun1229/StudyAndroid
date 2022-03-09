package com.lqk.activity.service

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * @date 2018/9/17
 * @time 9:12
 * @remarks
 */
class FirstIntentService : IntentService {

    companion object {
        const val TAG = "FirstIntentService"
    }

    constructor(name: String) : super(name)

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "Thread ${Thread.currentThread().id}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "---- onDestroy() 销毁 服务 ----")
    }
}