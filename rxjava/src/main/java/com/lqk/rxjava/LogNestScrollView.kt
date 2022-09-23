package com.lqk.rxjava

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

/**
 * @author LQK
 * @time 2022/8/11 22:21
 *
 */
class LogNestScrollView : NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        const val TAG = "LogNestScrollView"


    }

    private var lock: Any = Any()

    private var mListener: OnScrollListener? = null

    fun initScrollListener(l: OnScrollListener) {
        this.mListener = l
    }

    fun startScroll() {
        mListener?.start()
    }

    fun stopScroll() {
        mListener?.stop()
    }

    private var lastY = 0

    private var mHandle = @SuppressLint("HandlerLeak")
    object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (lastY == scrollY) {
                // 停止了
                Log.d(TAG, "handleMessage: $lastY : $scrollY")
                Log.d(TAG, "handleMessage: stop")
                stopScroll()
            } else {
                Log.d(TAG, "handleMessage: $lastY : $scrollY")
                sendEmptyMessageDelayed(-1, 10)
                lastY = scrollY
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            mHandle.sendEmptyMessageDelayed(-1, 10)
        } else if (ev.action == MotionEvent.ACTION_DOWN) {
            startScroll()
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
//        Log.d(TAG, "onScrollChanged: $t")
//        if (!isScrolling) {
//            startScroll()
//            mHandle.sendEmptyMessageDelayed(-1, 10)
//        }

    }

    interface OnScrollListener {
        fun start() {}
        fun stop() {}
    }
}