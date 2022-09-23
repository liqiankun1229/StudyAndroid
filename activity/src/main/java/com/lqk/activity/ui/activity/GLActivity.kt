package com.lqk.activity.ui.activity

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.gl.CustomGLSurfaceView

/**
 * @author LQK
 * @time 2022/4/9 9:36
 *
 */
class GLActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = CustomGLSurfaceView(this)
        setContentView(glSurfaceView)
    }
}