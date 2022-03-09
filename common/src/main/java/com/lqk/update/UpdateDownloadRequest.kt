package com.lqk.update

/**
 * @author LQK
 * @time 2019/9/17 9:46
 * @remark
 */
class UpdateDownloadRequest : Runnable {

    companion object{
        // 强制更新
        
        // 提升更新
    }

    private var downloadUrl = ""
    private var localFilePath = ""
    private var downloadListener: DownloadListener? = null

    override fun run() {

    }


    interface DownloadListener {
        fun downloadSucceed()
    }
}