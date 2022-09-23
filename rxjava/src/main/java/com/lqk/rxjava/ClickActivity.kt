package com.lqk.rxjava

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lqk.rxjava.databinding.ActivityClickBinding
import com.scwang.smart.refresh.header.ClassicsHeader

class ClickActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ClickActivity"
    }

    private lateinit var vb: ActivityClickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityClickBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_click)
        setContentView(vb.root)
        initView()
        initListener()
    }

    inner class UnPeek {
        var value: MutableMap<String, String>? = mutableMapOf()
    }

    private fun initView() {
        var header = ClassicsHeader(this)
        var s = UnPeek().value ?: mutableMapOf()

//        vb.srl.setRefreshHeader()
        vb.srl.setHeaderInsetStart(52f)
        vb.srlHeader.setPrimaryColors(Color.BLUE, Color.CYAN)
        vb.srlHeader.initOnMoveListener(object : TaoBaoHeader.OnMoveListener {
            override fun moveListener(i: Float) {
                vb.flToolbar.alpha = 1 - i
            }
        })
        // 下拉刷新控制
        vb.srl.setHeaderTriggerRate(0.4f)

        vb.srl.setOnRefreshListener {
            vb.fl.visibility = View.GONE
            vb.srl.finishRefresh(2000)
            vb.fl.visibility = View.VISIBLE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        vb.lnsv.initScrollListener(object : LogNestScrollView.OnScrollListener {

            override fun start() {
                super.start()
                Log.d(TAG, "start: 开始滚动")
                // 记录状态
            }

            override fun stop() {
                super.stop()
                val rect1 = Rect()
                vb.tv1.getLocalVisibleRect(rect1)
                if (rect1.top >= 0 && (rect1.top == 0 || rect1.top <= (vb.tv1.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv1 ${rect1.top} : ${vb.tv1.height}")
                }
                val rect2 = Rect()
                vb.tv2.getLocalVisibleRect(rect2)
                if (rect2.top >= 0 && (rect2.top == 0 || rect2.top <= (vb.tv2.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv2 ${rect2.top} : ${vb.tv2.height}")
                }
                val rect3 = Rect()
                vb.tv3.getLocalVisibleRect(rect3)
                if (rect3.top >= 0 && (rect3.top == 0 || rect3.top <= (vb.tv3.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv3 ${rect3.top} : ${vb.tv3.height}")
                }
                val rect4 = Rect()
                vb.tv4.getLocalVisibleRect(rect4)
                if (rect4.top >= 0 && (rect4.top == 0 || rect4.top <= (vb.tv4.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv4 ${rect4.top} : ${vb.tv4.height}")
                }
                val rect5 = Rect()
                vb.tv5.getLocalVisibleRect(rect5)
                if (rect5.top >= 0 && (rect5.top == 0 || rect5.top <= (vb.tv5.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv5 ${rect5.top} : ${vb.tv5.height}")
                }
                val rect6 = Rect()
                vb.tv6.getLocalVisibleRect(rect6)
                if (rect6.top >= 0 && (rect6.top == 0 || rect6.top <= (vb.tv6.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv6 ${rect6.top} : ${vb.tv6.height}")
                }
                val rect7 = Rect()
                vb.tv7.getLocalVisibleRect(rect7)
                if (rect7.top >= 0 && (rect7.top == 0 || rect7.top <= (vb.tv7.height / 2))) {
                    // 曝光
                    Log.d(TAG, "stop:tv7 ${rect7.top} : ${vb.tv7.height}")
                }
            }
        })
    }
}