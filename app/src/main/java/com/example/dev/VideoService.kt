package com.example.dev

import android.app.Service
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.IBinder

class VideoService : Service() {

    companion object {
        const val TAG = "VideoService"
    }

    private var mMediaProjection: MediaProjection? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mVirtualDisplay: VirtualDisplay? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}