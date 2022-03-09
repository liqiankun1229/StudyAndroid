package com.lqk.http

/**
 * @author lqk
 * @date 2018/5/26
 * @time 13:31
 * @remarks
 */
interface DownloadListener {
    // 进度
    fun onProgress(progress: Int)

    // 请求成功
    fun onSuccess()

    // 请求失败
    fun onFailed()

    // 取消请求
    fun onPaused()

    // 关闭
    fun onCanceled()

}
