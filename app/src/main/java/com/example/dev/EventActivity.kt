package com.example.dev

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dev.databinding.ActivityEventBinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * 注册消息
 */
class EventActivity : AppCompatActivity() {

    private lateinit var vm: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_event)
        vm = ActivityEventBinding.inflate(layoutInflater)
        setContentView(vm.root)
        vm.btn.setOnClickListener {
            startActivity(Intent(this, Event2Activity::class.java))
        }
        initEvent()
    }

    private fun initEvent() {
        LiveEventBus.get<EventBean>("EVENT").observe(this) {
            if (it.isSucceed) {
                Toast.makeText(this, "${it.type} 认证成功", Toast.LENGTH_SHORT).show()
                Event1Activity.show(this)
            } else {
                Toast.makeText(this, "${it.type} 认证失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}