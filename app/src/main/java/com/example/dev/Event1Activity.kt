package com.example.dev

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dev.databinding.ActivityEvent1Binding
import com.jeremyliao.liveeventbus.LiveEventBus

class Event1Activity : AppCompatActivity() {
    companion object {
        const val CODE = 0x0001
        fun show(activity: Activity) {
            activity.startActivity(Intent(activity, Event1Activity::class.java))
        }
    }

    private lateinit var vm: ActivityEvent1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_event1)

        vm = ActivityEvent1Binding.inflate(layoutInflater)
        setContentView(vm.root)
        vm.btn2.setOnClickListener {
            startActivityForResult(
                Intent(this, Event2Activity::class.java),
                CODE
            )
        }
        vm.btnFailed.setOnClickListener {
            LiveEventBus.get<EventBean>("EVENT").postDelay(EventBean(1, false, hashMapOf()), 500)
            finish()
        }
        vm.btnSucceed.setOnClickListener {
            LiveEventBus.get<EventBean>("EVENT").postDelay(EventBean(1, true, hashMapOf()), 500)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CODE -> {
                    // 认证结果
                    val isSucceed = data?.getBooleanExtra("isSucceed", false)
                    if (isSucceed == true) {
                        // 成功
                        LiveEventBus.get<EventBean>("EVENT").postDelay(
                            EventBean(1, true, hashMapOf()), 500
                        )
                        finish()
                    } else {
                        // 失败
                        Toast.makeText(this, "认证失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}