package com.lqk.net

interface DisposeDownloadListener : DisposeDataListener {
    fun onProgress(progress: Int)
}
