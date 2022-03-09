package com.lqk.activity.data

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.lqk.activity.common.Constacts
import com.lqk.activity.utils.ApkUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile


/**
 * @date 2018/9/18
 * @time 10:27
 * @remarks
 */
@Suppress("UNREACHABLE_CODE")
class DownloadTask : AsyncTask<String, Int, Int> {

    companion object {
        // 下载状态
        const val TYPE_SUCCESS = 0
        const val TYPE_FAILED = 1
        const val TYPE_PAUSED = 2
        const val TYPE_CANCELED = 3
    }

    // 下载进度监听
    private var listener: DownLoadListener

    // 是否关闭
    private var isCanceled = false

    // 是否暂停
    private var isPaused = false

    // 下载进度
    private var lastProgress: Int = 0

    constructor(listener: DownLoadListener) {
        this.listener = listener
        isCanceled = false
        isPaused = false
    }

    override fun doInBackground(vararg params: String?): Int {

        //  下载文件
        var inputStream: InputStream? = null
        var saveFile: RandomAccessFile? = null
        var file: File? = null
        try {
            var downloadLength: Long = 0
            val downloadUrl = params[0]
//            val fileName = downloadUrl!!.substring(downloadUrl.lastIndexOf("/"))
            val fileName = "杭州办事.apk"

            Constacts.apkName = fileName
            val downDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            file = File("$downDirectory/$fileName")
            Log.d("download", file.path)
            if (file.exists()) {
                // 如果文件已经存在，先删除
                file.delete()
                downloadLength = file.length()
            }
            val contentLength = getContentLength(downloadUrl!!)

            if (contentLength == 0L) {
                // 下载失败
                return TYPE_FAILED
            } else if (contentLength == downloadLength) {
                return TYPE_SUCCESS
            }

            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                    .addHeader("RANGE", "bytes=$downloadLength-")
//                    .addHeader("Content-Type", "application/vnd.android.package-archive")
                    .url(downloadUrl)
                    .build()
            val response = okHttpClient.newCall(request).execute()
            if (response.body != null) {
                inputStream = response.body!!.byteStream()
                saveFile = RandomAccessFile(file, "rw")
                saveFile.seek(downloadLength) // 跳过已经下载的字节
                var byte = ByteArray(1024)
                var total = 0
                var len: Int = inputStream.read(byte)
                while (len != -1) {
                    if (len == -1) {
                        break
                    }
                    if (isCanceled) {
                        return TYPE_CANCELED
                    } else if (isPaused) {
                        return TYPE_PAUSED
                    } else {
                        total += len
                        saveFile.write(byte, 0, len)
                        // 计算 已经下载 文件 的百分比
                        var progress = ((total.toFloat() + downloadLength) * 100 / contentLength).toInt()
                        if (progress > 100) {
                            progress = 100
                        }
                        publishProgress(progress)
                        len = inputStream.read(byte)
                    }
                }
                response.body!!.close()
                ApkUtil.setApkPermission("${Constacts.apkFilePath}/${Constacts.apkName}")
                return TYPE_SUCCESS
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (saveFile != null) {
                    saveFile.close()
                }
                if (isCanceled && file != null) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return TYPE_FAILED
    }

    override fun onProgressUpdate(vararg values: Int?) {
//        super.onProgressUpdate(*values)
        val progression = values[0]
        if (progression!! > lastProgress) {
            listener.onProgress(progression)
            lastProgress = progression
        }
    }

    override fun onPostExecute(result: Int?) {
//        super.onPostExecute(result)
        when (result!!) {
            TYPE_FAILED -> {
                listener.onFailed()
            }
            TYPE_SUCCESS -> {
                listener.onSuccess()
            }
            TYPE_PAUSED -> {
                listener.onPaused()
            }
            TYPE_CANCELED -> {
                listener.onCanceled()
            }
            else -> {
            }
        }
    }

    fun pauseDownload() {
        isPaused = true
    }

    fun cancelDownload() {
        isCanceled = true
    }

    fun getContentLength(downloadUrl: String): Long {
        val client = OkHttpClient()
        val request = Request.Builder().url(downloadUrl).build()
        val response = client.newCall(request).execute()
        if (response != null && response.isSuccessful) {
            val contentLength = response.body!!.contentLength()
            response.body!!.close()
            return contentLength
        }
        return 0
    }

}