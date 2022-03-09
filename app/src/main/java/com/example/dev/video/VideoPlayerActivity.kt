package com.example.dev.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dev.R

class VideoPlayerActivity : AppCompatActivity() {

    companion object {

        private const val KEY_URL = "KEY_URL"
        private const val KEY_TITLE = "KEY_TITLE"

        fun start(context: Context, url: String, title: String) {
            val videoPlayerActivity = Intent(context, VideoPlayerActivity::class.java)
            videoPlayerActivity.putExtra(KEY_URL, url)
            videoPlayerActivity.putExtra(KEY_TITLE, title)
            context.startActivity(videoPlayerActivity)
        }
    }

    var videoView: IjkVideoView? = null
    var mediaController: AndroidMediaController? = null
    var settings: Settings? = null
    private var url: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(KEY_TITLE) ?: ""
        url = intent.getStringExtra(KEY_URL) ?: ""
        if (url == "") {
            finish()
            return
        }
        setContentView(R.layout.activity_video_player)

        mediaController = AndroidMediaController(this, false)
        videoView = findViewById(R.id.video_view)
        videoView?.setMediaController(mediaController)
        videoView?.setVideoPath(url)

        videoView?.start()

    }

    override fun onStop() {
        super.onStop()
        videoView?.stopPlayback()
        videoView?.stopPlayback()
    }
}