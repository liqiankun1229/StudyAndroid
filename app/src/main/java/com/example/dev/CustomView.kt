package com.example.dev

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

/**
 * @author LQK
 * @date 2021/11/26 10:04
 * @remark
 */
class CustomView : View {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }


    // 画笔
    // style
    private var paint: Paint = Paint()

    // 路径
    private var mPath = Path()

    /**
     * @param style 绘制模式
     * @param color 画笔颜色
     * @param strokeWidth 线条宽度
     * @param textSize 文字大小
     * @param isAntiAlias 抗锯齿
     */
    private fun createPaint(style: Paint.Style, @ColorInt color: Int, strokeWidth: Float, textSize: Float, isAntiAlias: Boolean): Paint {
        paint.style = style
        paint.color = color
        paint.strokeWidth = strokeWidth
        paint.textSize = textSize
        paint.isAntiAlias = isAntiAlias
        return paint
    }

    // 纯白 不透明
    private var maskColor = Color.argb(0, 255, 255, 255)
    // 镂空圆形

    // 镂空边框
    private var border = Paint(Paint.ANTI_ALIAS_FLAG)

    // 镂空区域
    private var eraser = Paint(Paint.ANTI_ALIAS_FLAG)
    var duffOver = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
    var duffOut = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var path: Path = Path()

    private fun initView(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) {
        // 布局
        // 属性 attr
        // 赋值
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        // 边框
        border.style = Paint.Style.STROKE
        border.strokeWidth = 4f
        border.color = Color.parseColor("#00ff00")

    }

    private var centerX = 0
    private var centerY = 0
    private var w = 0
    private var h = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        centerX = w / 2
        centerY = h / 2
    }

    private var rectF: RectF = RectF()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 绘制圆弧
//        canvas?.drawArc()
        // 绘制位图
//        canvas?.drawBitmap()
        // 绘制圆
//        canvas?.drawCircle()
        paint.color = Color.parseColor("#333333")
        rectF.left = 0f
        rectF.top = 0f
        rectF.right = w.toFloat()
        rectF.bottom = h.toFloat()
        canvas?.save()
        path.addRect(rectF, Path.Direction.CW)
//        path.addCircle(300f, 300f, 200f, Path.Direction.CCW)
        canvas?.clipPath(path)
        canvas?.drawRect(rectF, paint)
        canvas?.restore()
        eraser.xfermode = duffOut
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), 200f, eraser)
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), 210f, border)
    }


}