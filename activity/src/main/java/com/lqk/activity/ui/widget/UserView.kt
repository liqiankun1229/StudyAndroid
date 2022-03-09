package com.lqk.activity.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @author LQK
 * @time 2022/1/10 17:03
 * @remark
 */
class UserView : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr, 0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        context?.let { c ->
            initView(c, attrs, defStyleAttr, defStyleRes)
        }
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        // attrs 需要自定义参数

    }

    // 画笔
    private var basePaint: Paint = Paint()

    // 路径
    var basePath: Path = Path()

    // 圆
    private var p = Paint()

    // rect rectF
    var rectF: RectF = RectF()


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 自定义 view 1 - 自定义 绘制
        // 主要方法 canvas.drawXXX()
        // canvas 画布
        // canvas 辅助类 范围裁剪 和 几何变换 , 可以使用不同的绘制方法控制 覆盖关系
        // 画布坐标方向 左->右 X: 0->(设备宽度); 上->下 Y: 0->(设备高度)
        // 颜色填充 整个 view
        canvas?.drawColor(Color.parseColor("#FFFFFF"))
        // canvas?.drawRGB(125, 125, 125) -> canvas?.drawColor(Color.rgb(125, 125, 125))
        // canvas?.drawARGB(125, 125, 125, 125) -> canvas?.drawColor(Color.argb(125,125, 125, 125))
        canvas?.save()
        // 画笔只设置颜色的情况, 就进行绘制(默认 FILL 填充模式), 会对绘制目标进行填充
        // 画笔绘制模式 - STROKE 画线 \ FILL 填充 \ FILL_AND_STROKE 画线并填充
        basePaint.style = Paint.Style.STROKE
        // 画笔颜色
        basePaint.color = Color.parseColor("#333333")
        // 画笔线条宽度
        basePaint.strokeWidth = 4f
        // 画笔文字大小
        basePaint.textSize = 24f
        // 画笔抗锯齿开关 - Paint 默认开启抗锯齿(31源码)
        basePaint.isAntiAlias = true
        // 画一个圆 中心 (100, 100) 半径 50 画笔 basePaint
        p.color = Color.RED
        canvas?.drawCircle(300f, 300f, 150f, basePaint)
        canvas?.drawCircle(300f, 300f, 150f, p)
        // 绘制一个矩形 --
        p.style = Paint.Style.FILL
        canvas?.drawRect(500f, 200f, 800f, 400f, p)
        // canvas?.drawRect(Rect, Paint) -> canvas?.drawRect(l,t,r,b,Paint)
        // canvas?.drawRect(RectF, Paint) -> canvas?.drawRect(l,t,r,b,Paint)
        // 绘制一个 点
        p.strokeWidth = 10f
        p.color = Color.BLACK
        p.strokeCap = Paint.Cap.ROUND
        canvas?.drawPoint(300f, 300f, p)
        p.strokeCap = Paint.Cap.SQUARE
        canvas?.drawPoint(320f, 300f, p)
        p.strokeCap = Paint.Cap.BUTT
        canvas?.drawPoint(340f, 300f, p)
        // canvas?.drawPoints(pts, p)
        // canvas?.drawPaints(pts, offset, count, p)
        canvas?.drawPoints(floatArrayOf(50f, 20f, 50f, 40f), p)
        // offset 跳过点的个数, 也就是 pts 中前端 offset*2 个数据被忽略, count, 开始绘制点的数量
        canvas?.drawPoints(floatArrayOf(100f, 20f, 100f, 40f, 100f, 60f, 100f, 80f), 2, 3, p)
        // 上面代码 等价于 实际绘制的点的个数 == (pts.size / 2)
        // canvas?.drawPoints(floatArrayOf(100f, 60f, 100f), p)
        /**
         * @see Canvas (BaseCanvas.java)
         */
        // canvas?.drawPoints(FloatArray, Paint) -> canvas?.drawPoints(FloatArray, 0, FloatArray.length, Paint)

        // 绘制椭圆 当 r-l == b-t 时, 绘制结果就是个圆
        canvas?.drawOval(200f, 500f, 400f, 700f, p)
        canvas?.drawOval(500f, 500f, 700f, 600f, p)
        // canvas?.drawOval(RectF, Paint) -> canvas?.drawOval(l,t,r,b,Paint)

        // 绘制 直线
        canvas?.drawLine(200f, 700f, 300f, 800f, p)
        // 实际绘制个数 FloatArray / 4 (4: 起点坐标(x,y) 终点坐标(x1,y1)) 不够四个的不进行绘制
        // canvas?.drawLines(FloatArray, Paint) -> canvas?.drawLines(FloatArray, 0, FloatArray.length,Paint)
        // 绘制直线 api 类似与 绘制点坐标

        // 绘制圆角矩形
        // rectF
        canvas?.drawRoundRect(100f, 900f, 300f, 1000f, 10f, 10f, p)
        p.style = Paint.Style.STROKE
        p.strokeCap = Paint.Cap.ROUND
        rectF.left = 500f
        rectF.top = 900f
        rectF.right = 700f
        rectF.bottom = 1000f
        canvas?.drawRoundRect(rectF, 10f, 10f, p)
        // canvas?.drawRoundRect(RectF, rx, ry, Paint) -> canvas?.drawRoundRect(l,t,r,b,rx,ry,Paint)

        // 绘制弧形 / 扇形 中心点向右平行 为 角度 0 前面确定整个圆(椭圆) 的边界
        canvas?.drawArc(100f, 1010f, 200f, 1110f, 0f, 80f, true, p)
        p.style = Paint.Style.FILL
        canvas?.drawArc(300f, 1010f, 500f, 1210f, 0f, 260f, true, p)
        // canvas?.drawArc(RectF, start, sweep, Paint) -> canvas?.drawArc(l,t,r,b,start,sweep,Paint)

        // 路径操作 添加路径
        // 添加圆
        // basePath.addCircle()
        // 添加椭圆
        // basePath.addOval()
        //  添加矩形
        // basePath.addRect()
        // 添加圆角矩形
        // basePath.addRoundRect()
        // 添加另一个 path
        // basePath.addPath()

        // 添加弧形
        basePath.addArc(100f, 1220f, 200f, 1320f, -180f, 180f)
        // 绘制
        basePath.arcTo(200f, 1220f, 300f, 1320f, -180f, 180f, false)

        // 添加直线
        basePath.lineTo(200f, 1420f)
        basePath.lineTo(100f, 1270f)
        // 添加 二次贝塞尔曲线(通过 起点, 控制点, 终点来描述一条曲线), 入参为 控制点和终点 (起点为当前点)默认在起点(0,0)
        // basePath.quadTo()
        // basePath.rQuadTo()
        // 添加 三次贝塞尔曲线
        // basePath.cubicTo()
        // basePath.rCubicTo()
        // 移动到目标位置 修改绘制下一个图形的起点
        // basePath.moveTo()

        // 封闭图形 (划线模式下, 是一个封闭的图形) 相当于进行 lineTo(当前点, 起点)
        basePath.close()


        p.style = Paint.Style.STROKE
        // 画笔 paint 填充模式, 效果类似自动闭合, 描边模式下 闭合需要起始点和终点重合
        // 绘制路径
        canvas?.drawPath(basePath, p)

        // Path 方法第二类: 辅助的 设置 和 计算
        basePath.fillType = Path.FillType.EVEN_ODD
        //




        // PorterDuff.Mode https://developer.android.com/reference/android/graphics/PorterDuff.Mode


    }
}