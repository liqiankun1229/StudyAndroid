package com.example.dev

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dev.databinding.ActivityEvent2Binding
import com.jeremyliao.liveeventbus.LiveEventBus

class Event2Activity : AppCompatActivity() {

    companion object {
        const val CODE = 0x0002
    }

    private lateinit var vm: ActivityEvent2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_event2)
        vm = ActivityEvent2Binding.inflate(layoutInflater)
        setContentView(vm.root)
        vm.btnSucceed.setOnClickListener {
            LiveEventBus.get<EventBean>("EVENT").post(EventBean(1, true))
//            setResult(RESULT_OK, Intent().putExtra("isSucceed", true))
//            finish()
        }
        vm.btnFailed.setOnClickListener {
            // 失败
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