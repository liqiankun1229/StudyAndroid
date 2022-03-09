package com.lqk.net

import android.os.Handler
import android.os.Looper
import android.os.Message
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


/**
 * @author LQK
 * @time 2019/3/11 16:57
 * @remark
 */
class NetFileCallback : Callback {

    /**
     * java 层异常，不一样的逻辑错误
     */
    protected val NETWORK_ERROR = -1 // 网络相对误差
    protected val IO_ERROR = -2      // JSON相对错误
    protected val EMPTY_MSG = ""
    /**
     * 将其它线程的数据转发到UI线程
     */
    private val PROGRESS_MESSAGE = 0x01
    private var mDeliveryHandler: Handler? = null
    private var mListener: DisposeDownloadListener? = null
    private var mFilePath: String = ""
    private var mProgress: Int = 0

    constructor(handle: DisposeDataHandle) : super() {
        this.mListener = handle.mListener as DisposeDownloadListener
        this.mFilePath = handle.mSource
        this.mDeliveryHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PROGRESS_MESSAGE -> mListener?.onProgress(msg.obj as Int)
                }
            }
        }
    }

    @Throws(IOException::class)
    override fun onFailure(call: Call, e: IOException) {
        mDeliveryHandler?.post { mListener?.onFailure(NetException(NETWORK_ERROR, e)) }
    }

    override fun onResponse(call: Call, response: Response) {
        val file = handleResponse(response)
        mDeliveryHandler?.post {
            if (file != null) {
                mListener?.onSuccess(file)
            } else {
                mListener?.onFailure(NetException(IO_ERROR, EMPTY_MSG))
            }
        }
    }

    /**
     * 此时还在子线程中，不则调用回调接口
     *
     * @param response
     * @return
     */
    private fun handleResponse(response: Response?): File? {
        if (response == null) {
            return null
        }

        var inputStream: InputStream? = null
        var file: File? = null
        var fos: FileOutputStream? = null
        val buffer = ByteArray(2048)
        var length: Int
        var currentLength = 0
        val sumLength: Double
        try {
            checkLocalFilePath(mFilePath)
            file = File(mFilePath)
            fos = FileOutputStream(file)
            inputStream = response.body!!.byteStream()
            sumLength = response.body!!.contentLength().toDouble()
            length = inputStream!!.read(buffer)
            while (length != -1) {
                fos.write(buffer, 0, length)
                currentLength += length
                mProgress = (currentLength / sumLength * 100).toInt()
                mDeliveryHandler?.obtainMessage(PROGRESS_MESSAGE, mProgress)?.sendToTarget()
                length = inputStream.read(buffer)
            }
            fos.flush()
        } catch (e: Exception) {
            file = null
        } finally {
            try {
                fos?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return file
    }

    private fun checkLocalFilePath(localFilePath: String) {
        val path = File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1))
        val file = File(localFilePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}