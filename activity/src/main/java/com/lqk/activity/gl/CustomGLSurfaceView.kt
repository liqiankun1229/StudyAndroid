package com.lqk.activity.gl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * @author LQK
 * @time 2022/4/9 12:51
 *
 */
class CustomGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: CustomGLRenderer

    init {
        // 设置版本
        setEGLContextClientVersion(2)
        // 初始化 renderer
        renderer = CustomGLRenderer()
        // 给GLSurfaceView 设置 renderer
        setRenderer(renderer)

        // 启动连续渲染
//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    companion object {
        private const val TOUCH_SCALE_FACTOR = 180f / 320f
    }

    private var preX: Float = 0f
    private var preY: Float = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val x: Float = event.x
        val y: Float = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.isTouch = true
            }
            MotionEvent.ACTION_MOVE -> {
                var dx = x - preX
                var dy = y - preY
                if (y > height / 2) {
                    dx *= -1
                }
                if (x < width / 2) {
                    dy *= -1
                }
                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
            MotionEvent.ACTION_UP -> {
                renderer.isTouch = false
            }
        }
        preX = x
        preY = y

        return true
//        return super.onTouchEvent(event)
    }

}