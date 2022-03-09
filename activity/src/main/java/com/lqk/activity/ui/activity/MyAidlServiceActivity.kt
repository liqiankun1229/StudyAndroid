package com.lqk.activity.ui.activity

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.R

class MyAidlServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_aidl_service)
        var gl = GLSurfaceView(this)
    }
}