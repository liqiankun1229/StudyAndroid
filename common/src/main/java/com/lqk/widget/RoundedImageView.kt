package com.lqk.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


/**
 * @author LQK
 * @time 2019/5/22 19:41
 * @remark 有圆角的 ImageView
 */
class RoundedImageView : AppCompatImageView {

    private var isCircle: Boolean = false

    private var xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun initView() {}


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas ?: return
//        canvas.saveLayer(srcRectF, null, Canvas.ALL_SAVE_FLAG)
        drawBorders(canvas)

    }

    private fun drawBorders(canvas: Canvas) {
        if (isCircle) {

        } else {

        }
    }


}