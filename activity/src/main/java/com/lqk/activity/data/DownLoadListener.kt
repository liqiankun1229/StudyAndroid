package com.lqk.activity.data

/**
 * @date 2018/9/18
 * @time 10:24
 * @remarks
 */
interface DownLoadListener {

    // 下载进度
    fun onProgress(progress: Int)

    // 下载成功
    fun onSuccess()

    // 下载失败
    fun onFailed()

    // 暂停下载
    fun onPaused()

    // 关闭下载
    fun onCanceled()

}