package com.example.dev

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * *手机录屏*
 * 开始录制
 * 暂停录制
 * 停止录制
 * 录屏设置
 * 关闭
 */
class VideoActivity : AppCompatActivity() {
    companion object {
        const val CODE = 0x000100

        fun start(context: Context) {
            context.startActivity(Intent(context, VideoActivity::class.java))
        }
    }

    private lateinit var mMediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        // 权限
        mMediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    /**
     * 开始录屏
     */
    fun startRecorder(view: View) {
        // 开始录屏服务
        createScreen()
    }

    /**
     * 暂停录屏
     */
    fun parseRecorder(view: View) {
        // 停止录屏服务
        Toast.makeText(this, "暂停录屏", Toast.LENGTH_SHORT).show()
    }

    /**
     * 停止录屏
     */
    fun stopRecorder(view: View) {
        Toast.makeText(this, "停止录屏", Toast.LENGTH_SHORT).show()
    }

    private fun createScreen() {
        var capture = mMediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(capture, CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CODE -> {
                    Toast.makeText(this, "开始录频", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun startSetting(view: View) {
        Toast.makeText(this, "录屏设置", Toast.LENGTH_SHORT).show()
    }

    fun close(view: View) {
        Toast.makeText(this, "关闭录屏", Toast.LENGTH_SHORT).show()
    }
}