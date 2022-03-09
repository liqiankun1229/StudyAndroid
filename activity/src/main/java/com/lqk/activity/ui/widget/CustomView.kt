package com.lqk.activity.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import com.lqk.activity.R

/**
 * @author LQK
 * @time 2018/12/19 12:28
 * @remark 自定义view
 */
class CustomView : View {

    companion object {
        private const val TAG = "CustomView"
    }

    /**
     * view 类型
     */
    enum class ViewType {
        TitleView,
        ImageView,
        CheckView,

    }

    var text: String = ""
    var imageAsset: String = ""
    var imageId: Int = -1

    var progressStartColor: Int? = null
    var progressEndColor: Int? = null
    var progressBgStartColor: Int? = null
    var progressBgMidColor: Int? = null
    var progressBgEndColor: Int? = null

    var progress: Int? = null
    var progressWidth: Float? = null
    var startAngle: Int? = null
    var sweepAngle: Int? = null
    var showAnim: Boolean? = false

    var unitAngle: Float? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CustomView)
        progressStartColor = typedArray?.getColor(R.styleable.CustomView_pr_progress_start_color, Color.WHITE)
        progressEndColor = typedArray?.getColor(R.styleable.CustomView_pr_progress_end_color, Color.WHITE)
        progressBgStartColor = typedArray?.getColor(R.styleable.CustomView_pr_bg_start_color, Color.BLACK)
        progressBgMidColor = typedArray?.getColor(R.styleable.CustomView_pr_bg_mid_color, Color.BLACK)
        progressBgEndColor = typedArray?.getColor(R.styleable.CustomView_pr_bg_end_color, Color.BLACK)
        progress = typedArray?.getInt(R.styleable.CustomView_pr_progress, 0)
        progressWidth = typedArray?.getDimension(R.styleable.CustomView_pr_progress_width, 8F)
        startAngle = typedArray?.getInt(R.styleable.CustomView_pr_start_angle, 150)
        sweepAngle = typedArray?.getInt(R.styleable.CustomView_pr_sweep_angle, 240)
        showAnim = typedArray?.getBoolean(R.styleable.CustomView_pr_show_anim, true)
        typedArray?.recycle()

        unitAngle = (sweepAngle!! / 100.0).toFloat()
        initView()

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        bgPaint.style = Paint.Style.STROKE
        bgPaint.strokeCap = Paint.Cap.ROUND
        bgPaint.strokeWidth = progressWidth ?: 8F

        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.strokeWidth = progressWidth ?: 8F
        this.setOnTouchListener { v, event ->
            run {
                return@setOnTouchListener true
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        return super.onTouchEvent(event)
    }

    var mMeasureWidth: Int? = null
    var mMeasureHeight: Int? = null

    var pRectF: RectF? = null

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mMeasureWidth = measuredWidth
        mMeasureHeight = measuredHeight
        if (pRectF == null) {
            val halfProgressWidth = progressWidth!! / 2
            pRectF = RectF(halfProgressWidth + paddingLeft,
                    halfProgressWidth + paddingTop,
                    mMeasureWidth!! - halfProgressWidth - paddingRight,
                    mMeasureHeight!! - halfProgressWidth - paddingBottom)
        }
    }

    /**
     * 布局
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    private var bgPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var progressPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var curProcess = 0

    /**
     * 绘制
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!showAnim!!) {
            curProcess = progress!!
        }

        // 绘制进度
        val drawEnd = (curProcess * unitAngle!!).toInt()
        for (j in 0..(drawEnd)) {
            progressPaint.color = getGradient(j / drawEnd.toFloat(), progressStartColor!!, progressEndColor!!)
            canvas?.drawArc(pRectF!!,
                    (startAngle!! + j).toFloat(),
                    1F,
                    false,
                    progressPaint)
        }

        // 绘制背景
        val halfSweep = sweepAngle!! / 2F
        val st = (curProcess * unitAngle!!).toInt()
        for (i in (sweepAngle!! - 1) downTo st) {
            if (i - halfSweep > 0) {
                bgPaint.color = getGradient((i - halfSweep) / halfSweep, progressBgMidColor!!, progressBgEndColor!!)
            } else {
                bgPaint.color = getGradient((halfSweep - i) / halfSweep, progressBgMidColor!!, progressBgStartColor!!)
            }
            canvas?.drawArc(pRectF!!,
                    (startAngle!! + i).toFloat(),
                    1F,
                    false,
                    bgPaint)
        }

        if (curProcess < progress!!) {
            curProcess++
            postInvalidate()
        }
    }

    fun setCurProgress(@IntRange(from = 0, to = 100) progress: Int) {
        this.progress = progress
        invalidate()
    }

    private fun getGradient(fraction: Float, startColor: Int, endColor: Int): Int {
        var startFraction = fraction
        if (startFraction > 1) {
            startFraction = 1F
        }
        val alphaStart = Color.alpha(startColor)
        val redColorStart = Color.red(startColor)
        val blueColorStart = Color.blue(startColor)
        val greenColorStart = Color.green(startColor)

        val alphaEnd = Color.alpha(endColor)
        val redColorEnd = Color.red(endColor)
        val blueColorEnd = Color.blue(endColor)
        val greenColorEnd = Color.green(endColor)

        val alphaDifference = alphaEnd - alphaStart
        val redDifference = redColorEnd - redColorStart
        val blueDifference = blueColorEnd - blueColorStart
        val greenDifference = greenColorEnd - greenColorStart

        val alphaCurrent = (alphaStart + startFraction * alphaDifference).toInt()
        val redCurrent = (redColorStart + startFraction * redDifference).toInt()
        val blueCurrent = (blueColorStart + startFraction * blueDifference).toInt()
        val greenCurrent = (greenColorStart + startFraction * greenDifference).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }

}