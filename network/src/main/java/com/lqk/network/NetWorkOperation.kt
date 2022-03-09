package com.lqk.network

import java.io.File

/**
 * @author LQK
 * @time 2019/4/15 8:45
 * @remark 网络请求类型操作
 */
interface NetWorkOperation {
    fun <T> doGet(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>,
        callback: NetWorkCallback<T>? = null
    )

    fun <T> doPost(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>,
        callback: NetWorkCallback<T>? = null
    )

    fun <T> doSyncPost(
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<T>
    ): T?

    fun doPut()

    fun doDelete()

    fun download(
        filePath: String,
        url: String,
        params: HashMap<String, Any>,
        resultClass: Class<File>, callback: OnDownLoadListener
    )

    interface OnDownLoadListener : NetWorkCallback<File> {

        /**
         * @param process 进度 0-100
         */
        fun downloadProcess(process: Int)
    }
}