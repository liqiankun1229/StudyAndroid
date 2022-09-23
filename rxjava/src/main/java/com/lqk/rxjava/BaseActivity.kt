package com.lqk.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author LQK
 * @time 2019/7/15 10:42
 * @remark
 */
@SuppressLint("Registered")
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    companion object {
        const val TAG = "BaseActivity"
        const val EVENT_LOGIN = "EVENT_LOGIN"
    }

    protected lateinit var vb: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    open fun initActivity() {
        vb = initVM()
        setContentView(vb.root)
        initView()
        initEvent()
    }

    abstract fun initVM(): VB
    open fun initView(){}

    private fun initEvent() {
        Log.d(TAG, "initEvent: 消息总线")
        LiveEventBus.get<String>(EVENT_LOGIN).observe(this) {
            Log.d(TAG, "initEvent: $it")
        }
    }

    override fun onResume() {
        super.onResume()
    }
}