package com.lqk.activity.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatButton
import com.lqk.activity.R


/**
 * @author LQK
 * @time 2019/4/10 15:18
 * @remark
 */
class RoundButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initCornerBackground(attrs!!)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initCornerBackground(attrs!!)
    }

    private var colorNormal: Int = 0
    private var cornerRadius: Float = 0.toFloat()
    private var bgDrawableNormal: RoundCornerDrawable? = null

    // 省略三个构造方法
    // 构造方法最后一定要调用initCornerBackground完成初始化
    private fun initCornerBackground(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton)
        this.cornerRadius = a.getDimension(R.styleable.RoundButton_rcb_cornerRadius, 0F)
        this.colorNormal = a.getColor(R.styleable.RoundButton_rcb_backgroundColor, 0)
        makeBackgroundDrawable()
        a.recycle()
    }

    fun setMBackgroundColor(mColor: Int) {
        this.colorNormal = mColor
        makeBackgroundDrawable()
        invalidate()
        requestLayout()
    }

    private fun makeBackgroundDrawable() {
        bgDrawableNormal = RoundCornerDrawable(this.colorNormal, this.cornerRadius)
        bgDrawableNormal!!.setRect(width, height)
        setBackgroundDrawable(bgDrawableNormal)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // layout之后必然会draw，所以在这里设置drawable的尺寸
        if (bgDrawableNormal != null) {
            bgDrawableNormal!!.setRect(right - left, bottom - top)
        }
    }

    private inner class RoundCornerDrawable(color: Int, private val radius: Float) : Drawable() {
        private var paint: Paint = Paint()
        private var rectF: RectF

        init {
            // 实心的画笔
            paint.style = Paint.Style.FILL
            paint.isAntiAlias = true
            paint.color = color
            this.rectF = RectF()
        }

        // 用于设置Drawable宽高
        fun setRect(width: Int, height: Int) {
            this.rectF.left = 0f
            this.rectF.top = 0f
            this.rectF.right = width.toFloat()
            this.rectF.bottom = height.toFloat()
        }

        override fun draw(@NonNull canvas: Canvas) {
            canvas.drawRoundRect(rectF, radius, radius, paint) // 画圆角矩形，现成的方法
        }

        // 其余方法略
        override fun setAlpha(alpha: Int) {
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
        }
    }

}