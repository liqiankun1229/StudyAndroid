package com.example.dev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.commit {
            add(R.id.fl_content, CameraFragment())
        }
    }
}