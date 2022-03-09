package com.lqk.activity.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * @author lqk
 * @date 2018/8/3
 * @time 14:09
 * @remarks
 */
class LoginBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "接受广播", Toast.LENGTH_SHORT).show()
    }
}