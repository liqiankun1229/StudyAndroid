package com.lqk.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.lqk.R

/**
 * @author LQK
 * @time 2019/4/29 15:33
 * @remark 站点View
 */
class StepView : View {

    private var defaultNormalLineColor: Int = 0
    private var defaultPassLineColor: Int = Color.BLACK
    private var defaultTextColor: Int = Color.BLACK
    private var defaultLineWidth: Float = 0f
    private var defaultTextSize: Float = 0f
    private var defaultText2DotMargin: Float = 0f
    private var defaultMargin: Float = 0f
    private var defaultLine2TopMargin: Float = 0f
    private var defaultText2BottomMargin: Float = 0f
    private var defaultViewClickable: Boolean = true

    private var defaultDotCount: Int = 5
    private var defaultStepNum: Int = 3
    private var defaultLineLength: Int = 2

    private var defaultMaxDotCount: Int = 100
    private var defaultTextLocation: Int = 5

    private lateinit var normalPic: Bitmap
    private lateinit var targetPic: Bitmap
    private lateinit var passedPic: Bitmap

    private var dotCount: Int = 5
    private var mStepNum: Int = 0
    private var mTarget: Int = 0
    private var lineLength: Int = 5

    private var maxDotCount: Int = 100
    private var textLocation: Int = 5

    private var isTextBelowLine: Boolean = false
    private var normalLineColor: Int = Color.WHITE
    private var passLineColor: Int = Color.BLACK
    private var lineWidth: Float = 2f

    private var mTextColor: Int = Color.BLACK
    private var mTextSize: Float = 2f

    private var text2LineMargin: Float = 0f
    private var mMargin: Float = 0f

    private var line2TopMargin: Float = 0f
    private var line2BottomMargin: Float = 0f
    private var text2TopMargin: Float = 0f
    private var text2BottomMargin: Float = 0f
    private var mClickable: Boolean = true

    // 画笔
    private lateinit var linePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var bounds: Rect

    private var perLineLength: Int = 0

    // 步骤点的宽高
    private var passWH: IntArray = IntArray(2)
    private var normalWH: IntArray = IntArray(2)
    private var targetWH: IntArray = IntArray(2)

    // 步骤说明文字 size 和 dot count 数量相等
    private var texts = arrayOf<String>()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        context ?: return
        defaultNormalLineColor = Color.parseColor("#545454")
        defaultPassLineColor = Color.BLACK
        defaultTextColor = Color.BLACK
        defaultLineWidth = dp2px(context, 1)
        defaultTextSize = sp2px(context, 80)
        defaultText2DotMargin = dp2px(context, 15)
        defaultMargin = dp2px(context, 100)
        defaultLine2TopMargin = dp2px(context, 30)
        defaultText2BottomMargin = dp2px(context, 20)
//        normalPic = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_normal)
        normalPic = getBitmapFromVectorDrawable(context, R.drawable.ic_normal)
//        targetPic = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_target)
        targetPic = getBitmapFromVectorDrawable(context, R.drawable.ic_target)
//        passedPic = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_passed)
        passedPic = getBitmapFromVectorDrawable(context, R.drawable.ic_passed)
        val a = context.obtainStyledAttributes(attrs, R.styleable.StepView, defStyleAttr, 0)
        a ?: return
        dotCount = a.getInt(R.styleable.StepView_count, defaultDotCount)
        if (dotCount < 2) {
            throw IllegalArgumentException("Steps can't be less than 2")
        }
        mStepNum = a.getInt(R.styleable.StepView_step, defaultStepNum)
        lineLength = a.getInt(R.styleable.StepView_line_length, defaultLineLength)
        maxDotCount = a.getInt(R.styleable.StepView_max_dot_count, defaultMaxDotCount)
        if (maxDotCount < dotCount) {// 当最多点小于设置点数量时，设置线条长度可变
            lineLength = defaultLineLength
        }
        mMargin = a.getDimension(R.styleable.StepView_margin, defaultMargin)
        textLocation = a.getInt(R.styleable.StepView_text_location, defaultTextLocation)
        isTextBelowLine = (textLocation == defaultTextLocation)
        normalLineColor = a.getColor(R.styleable.StepView_normal_line_color, defaultNormalLineColor)
        passLineColor = a.getColor(R.styleable.StepView_passed_line_color, defaultPassLineColor)
        lineWidth = a.getDimension(R.styleable.StepView_line_stroke_width, defaultLineWidth)
        mTextColor = a.getColor(R.styleable.StepView_text_color, defaultTextColor)
        mTextSize = a.getDimension(R.styleable.StepView_text_size, defaultTextSize)
        text2LineMargin = a.getDimension(R.styleable.StepView_text_to_line_margin, defaultText2DotMargin)
        line2TopMargin = a.getDimension(R.styleable.StepView_line_to_top_margin, defaultLine2TopMargin)
        text2BottomMargin = a.getDimension(R.styleable.StepView_text_to_bottom_margin, defaultText2BottomMargin)
        mClickable = a.getBoolean(R.styleable.StepView_is_view_clickable, defaultViewClickable)
        a.recycle()
        // 当文字在线条上面时，参数倒置
        if (!isTextBelowLine) {
            line2BottomMargin = line2TopMargin
            text2TopMargin = text2BottomMargin
        }
        // 线条画笔
        linePaint = Paint()
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = lineWidth
        // 文字画笔
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = defaultTextColor
        textPaint.textSize = mTextSize
        // 存放说明文字的矩形
        bounds = Rect()
    }

    /**
     * VSG -> Bitmap
     */
    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //
        val newWidth = (width - mMargin * 2).toInt()
        val newHeight = h
        perLineLength = if (lineLength == defaultLineLength) {
            width / (dotCount - 1)
        } else {
            width / (maxDotCount - 1)
        }
        passWH = calculateWidthAndHeight(passedPic)
        normalWH = calculateWidthAndHeight(normalPic)
        targetWH = calculateWidthAndHeight(targetPic)
        super.onSizeChanged(newWidth, newHeight, oldw, oldh)
    }

    private fun calculateWidthAndHeight(bitmap: Bitmap): IntArray {
        val wh = IntArray(2)
        val w = bitmap.width
        val h = bitmap.height
        wh[0] = w
        wh[1] = h
        return wh
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawConnectLine(canvas, mStepNum)
        drawNormalSquare(canvas, mStepNum)
        drawDescText(canvas)
    }

    /**
     * 绘制点与点之间的距离线
     */
    private fun drawConnectLine(canvas: Canvas?, stepNum: Int) {
        var startX: Float
        var stopX: Float
        for (i in 0 until dotCount - 1) {
            startX = when {
                i == stepNum -> {
                    mMargin + perLineLength * i + targetWH[0] / 2
                }
                i > stepNum -> {
                    mMargin + perLineLength * i + normalWH[0] / 2
                }
                else -> {
                    mMargin + perLineLength * i + passWH[0] / 2
                }
            }
            // 设置线条终点 X 轴 坐标
            stopX = when {
                i + 1 == stepNum -> {
                    mMargin + perLineLength * (i + 1) - targetWH[0] / 2
                }
                i + 1 < stepNum -> {
                    mMargin + perLineLength * (i + 1) - passWH[0] / 2
                }
                else -> {
                    mMargin + perLineLength * (i + 1) - normalWH[0] / 2
                }
            }
            // 当目标 步骤超过 i 时， 线条设置为已过颜色 不超过时：设置为普通颜色
            if (stepNum > i) {
                linePaint.color = passLineColor
            } else {
                linePaint.color = normalLineColor
            }
            if (isTextBelowLine) {
                canvas?.drawLine(startX, line2TopMargin, stopX, line2TopMargin, linePaint)
            } else {
                canvas?.drawLine(startX, height - line2BottomMargin, stopX, height - line2BottomMargin, linePaint)
            }
        }
    }

    /**
     * 绘制一般情况下的步骤点图片
     */
    private fun drawNormalSquare(canvas: Canvas?, stepNum: Int) {
        for (i in 0 until dotCount) {
            // 在目标点状态时， 普通图片不绘制
            when {
                stepNum == i -> {
                    mTarget = mStepNum
                    drawTargetSquare(canvas, mTarget)
//                    val l = mMargin + perLineLength * i - passWH[0] / 2
//                    val t: Float = if (isTextBelowLine) {
//                        line2TopMargin - targetWH[1] / 2
//                    } else {
//                        height - line2BottomMargin - targetWH[1] / 2
//                    }
//                    canvas?.drawBitmap(targetPic, l, t, null)
                }
                stepNum > i -> {
                    val l = mMargin + perLineLength * i - passWH[0] / 2
                    val t: Float = if (isTextBelowLine) {
                        line2TopMargin - passWH[1] / 2
                    } else {
                        height - line2BottomMargin - passWH[1] / 2
                    }
                    canvas?.drawBitmap(passedPic, l, t, null)
                }
                else -> {
                    val l = mMargin + perLineLength * i - normalWH[0] / 2
                    val t: Float = if (isTextBelowLine) {
                        line2TopMargin - normalWH[1] / 2
                    } else {
                        height - line2BottomMargin - normalWH[1] / 2
                    }
                    canvas?.drawBitmap(normalPic, l, t, null)
                }
            }
        }
    }

    /**
     * 画目标步骤点
     */
    private fun drawTargetSquare(canvas: Canvas?, target: Int) {
        val l = mMargin + perLineLength * target - targetWH[0] / 2
        val t = if (isTextBelowLine) {
            line2TopMargin - targetWH[1] / 2
        } else {
            height - line2BottomMargin - targetWH[1] / 2
        }
        canvas?.drawBitmap(targetPic, l, t, null)
    }

    /**
     * 绘制步骤说明文字
     */
    private fun drawDescText(canvas: Canvas?) {
        for (i in 0 until dotCount) {
            val text = texts[i]
            textPaint.getTextBounds(text, 0, text.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()
            val x = mMargin + perLineLength * i - textWidth / 2
            val y: Float
            y = if (isTextBelowLine) {
                height - text2BottomMargin
            } else {
                text2TopMargin + textHeight
            }
            canvas?.drawText(text, x, y, textPaint)
        }
    }

    // 偏移量
    private var mScrollX: Int = 0
    private var mScrollY: Int = 0

    override fun scrollTo(x: Int, y: Int) {
        if (mScrollX != x || mScrollY != y) {
            val oldX = mScrollX
            val oldY = mScrollY
            mScrollX = x
            mScrollY = y
            onScrollChanged(mScrollX, mScrollY, oldX, oldY)
            if (!awakenScrollBars()) {
                invalidate()
            }
        }
//        super.scrollTo(x, y)
    }

    override fun scrollBy(x: Int, y: Int) {
//        super.scrollBy(x, y)
        scrollTo(mScrollX + x, mScrollY)
    }

    override fun performClick(): Boolean {
        if (mClickable) {
            // 可点击
        }
        return super.performClick()
    }

    private var startPoint: Point = Point()

    /**
     * 重写点击事件
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mClickable) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    startPoint.x = event.x.toInt()
                    startPoint.y = event.y.toInt()
                    val stepInDots = getStepInDots(startPoint)
                    if (stepInDots != -1) {
                        mStepNum = stepInDots
                        invalidate()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    // 移动
                    scrollBy(event.x.toInt() - startPoint.x, event.y.toInt() - startPoint.y)
                }
                MotionEvent.ACTION_UP -> {
                    performClick()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getStepInDots(point: Point): Int {
        for (i in 0 until dotCount) {
            val rect = getStepSquareRects()[i] ?: return -1
            val x = point.x
            val y = point.y
            if (rect.contains(x, y)) {
                return i
            }
        }
        return -1
    }

    private fun getStepSquareRects(): Array<Rect?> {
        val rects = arrayOfNulls<Rect>(dotCount)
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until dotCount) {
            // 此处默认所有点的区域范围为被选中图片的区域范围
            val rect = Rect()
            left = (mMargin + perLineLength * i - targetWH[0] / 2).toInt()
            right = (mMargin + perLineLength * i + targetWH[0] / 2).toInt()
            if (isTextBelowLine) {
                top = (line2TopMargin - targetWH[1] / 2).toInt()
                bottom = (line2TopMargin + targetWH[1] / 2).toInt()
            } else {
                top = (height - line2BottomMargin - targetWH[1] / 2).toInt()
                bottom = (height - line2BottomMargin + targetWH[1] / 2).toInt()
            }
            rect.set(left, top, right, bottom)
            rects[i] = rect
        }
        return rects
    }

    private fun dp2px(context: Context?, value: Int): Float {
        val density = context?.resources?.displayMetrics?.density ?: 0f
        return (density * value + 0.5f)
    }

    private fun sp2px(context: Context?, value: Int): Float {
        val scaledDensity = context?.resources?.displayMetrics?.scaledDensity ?: 0f
        return (value / scaledDensity + 0.5f)
    }

    /**
     * 设置步骤数量
     */
    fun setDotCount(count: Int) {
        if (count < 2) {
            throw IllegalArgumentException("数量太少")
        }
        dotCount = count
    }

    /**
     * 设置步骤描述
     */
    fun setDescription(descs: Array<String>) {
        if (descs.isEmpty() || descs.size < dotCount) {
            throw IllegalArgumentException("Descriptions can't be null or its length must more than dot count")
        }
        texts = descs
    }

    /**
     * 设置View 是否可以点击
     */
    fun setMClickable(clickable: Boolean) {
        this.mClickable = clickable
    }

    /**
     * 设置步骤
     */
    fun setStep(step: Step) {
        when (step) {
            Step.ONE -> {
                mStepNum = 0
            }
            Step.TWO -> {
                mStepNum = 1
            }
            Step.THREE -> {
                mStepNum = 2
            }
            Step.FOUR -> {
                mStepNum = 3
            }
            Step.FIVE -> {
                mStepNum = 4
            }
        }
        invalidate()
    }

    /**
     * 枚举，步骤
     */
    enum class Step {
        ONE, TWO, THREE, FOUR, FIVE
    }
}