package com.lqk.mvp.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * @author lqk
 * @date 2018/7/30
 * @time 10:45
 * @remarks
 */
class MyScrollView : ScrollView {

    private lateinit var onScrollMove: OnScrollMove

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollMove.onChange(l, t, oldl, oldt)
    }

    fun setOnScrollView(onScrollMove: OnScrollMove) {
        this.onScrollMove = onScrollMove
    }

    interface OnScrollMove {
        fun onChange(l: Int, t: Int, oldl: Int, oldt: Int)
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY / 10)
    }
}