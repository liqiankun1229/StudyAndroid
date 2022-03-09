package com.lqk.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 * @author LQK
 * @time 2019/4/24 20:14
 * @remark
 */
class RoundedView : View {

    // 圆角
    private var topLeft:Float = 0f
    private var topRight = 0f
    private var bottomLeft = 0f
    private var bottomRight = 0f

    private var src: Drawable? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun initView(context: Context?, attrs: AttributeSet?) {

    }

}