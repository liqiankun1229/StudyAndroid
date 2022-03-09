package com.example.dev

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

/**
 * @author LQK
 * @date 2021/12/17 9:23
 * @remark 人脸遮罩 效果
 */
class FaceView : View {

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

    // 纯白 不透明 遮罩
    private var maskColor = Color.parseColor("#FFFFFF")
    // 镂空圆形

    // 镂空边框
    private var border = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderColor = Color.parseColor("#EEEEEF")

    // 镂空区域
    private var eraser = Paint(Paint.ANTI_ALIAS_FLAG)
    var duffOver = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
    var duffOut = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var path: Path = Path()

    private fun initView(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        // 边框
        border.style = Paint.Style.STROKE
        border.strokeWidth = 8f
        border.color = borderColor

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
        paint.color = maskColor
        rectF.left = 0f
        rectF.top = 0f
        rectF.right = w.toFloat()
        rectF.bottom = h.toFloat()
        // 绘制这招
        canvas?.save()
        path.addRect(rectF, Path.Direction.CW)
        canvas?.clipPath(path)
        canvas?.drawRect(rectF, paint)
        canvas?.restore()
        eraser.xfermode = duffOut
        // 镂空
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), min(this.w.toFloat(), this.h.toFloat()) / 2 - 30f, eraser)
        // 绘制边框
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), min(this.w.toFloat(), this.h.toFloat()) / 2 - 10f, border)
    }
}