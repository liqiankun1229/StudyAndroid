package com.lqk.activity.gl

import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author LQK
 * @time 2022/4/9 12:52
 *
 */
class CustomGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square

    // 该字段的写入立刻对其他线程可见
    @Volatile
    var angle: Float = 0f

    @Volatile
    var isTouch: Boolean = false

    /**
     * 访问着色器程序矩阵
     */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 设置背景颜色
        GLES32.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        // 初始化三角形
        mTriangle = Triangle()
        // 初始化正方形
//        mSquare = Square()
    }

    // 定义投影 vPMatrix 是 Model View Projection Matrix 的缩写
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    //
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
        // 投影计算
        val ratio = width.toFloat() / height.toFloat()
        // 将这个投影应用于 对象坐标
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    // 动画
    private val rotationMatrix = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        // 重置
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)

        // 设置相机的位置 (视图矩阵)
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, -3f,
            0f, 0f, 0f,
            0f, 1f, 0f
        )

        // 计算投影与视图的转换
        Matrix.multiplyMM(
            vPMatrix, 0,
            projectionMatrix, 0,
            viewMatrix, 0
        )

        // 创建一个旋转的三角形
        // 动画
        val scratch = FloatArray(16)
        if (!isTouch) {
            // 时间 -> 角度
            val time = SystemClock.uptimeMillis() % 4000L
            angle = 0.090f * time.toInt()
        }
        // 角度换算
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        // 旋转的三角形 动画
        Matrix.multiplyMM(
            scratch, 0,
            vPMatrix, 0,
            rotationMatrix, 0
        )

        // 绘制
//        mTriangle.draw(vPMatrix)
        mTriangle.draw(scratch)
//        mSquare.draw(vPMatrix)
//        mSquare.draw(scratch)
    }


}