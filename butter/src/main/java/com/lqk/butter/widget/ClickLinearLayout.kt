package com.lqk.butter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * @author LQK
 * @time 2019/8/27 15:39
 * @remark
 */
class ClickLinearLayout :LinearLayout{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return false
//        return super.onInterceptHoverEvent(event)
    }
}