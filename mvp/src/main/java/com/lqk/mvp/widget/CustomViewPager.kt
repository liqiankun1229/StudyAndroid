package com.lqk.mvp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @author LQK
 * @time 2019/5/11 9:33
 * @remark
 */
class CustomViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        return super.onTouchEvent(ev)
        return false
    }

//    override fun isNestedScrollingEnabled(): Boolean {
////        return super.isNestedScrollingEnabled()
//        return false
//    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        return super.onInterceptTouchEvent(ev)
        return false
    }
}