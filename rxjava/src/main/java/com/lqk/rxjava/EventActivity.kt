package com.lqk.rxjava

import android.app.Application
import android.content.Context
import android.content.Intent
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lqk.rxjava.databinding.ActivityEventBinding

class EventActivity : BaseActivity<ActivityEventBinding>() {
    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, EventActivity::class.java).also {
                if (context is Application){
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }

    override fun initVM(): ActivityEventBinding {
        return ActivityEventBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        vb.tvResult.setOnClickListener {
            finish()
            LiveEventBus.get<String>(EVENT_LOGIN).post("EventActivity")
        }
    }
}