package com.lqk.activity.update

import android.os.Environment
import com.library.base.BaseHttpDownloadManager
import com.library.listener.OnDownloadListener
import com.library.utils.FileUtil
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


/**
 * @author LQK
 * @time 2019/4/19 9:39
 * @remark
 */
class MyDownload : BaseHttpDownloadManager() {
    override fun download(apkUrl: String?, apkName: String?, listener: OnDownloadListener?) {
        listener?.start()
        try {
            val url = URL(apkUrl)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.readTimeout = 600000
            con.connectTimeout = 600000
            if (con.responseCode == HttpURLConnection.HTTP_OK) {
                val inStream = con.inputStream
                val length = con.getContentLength()
                //当前已下载完成的进度
                var progress = 0
                val buffer = ByteArray(1024 * 4)
                val file = FileUtil.createFile("${Environment.getExternalStorageDirectory()}/AppUpdate", apkName)
                val stream = FileOutputStream(file)
                var len = inStream.read(buffer)
                while ((len) != -1) {
                    //将获取到的流写入文件中
                    stream.write(buffer, 0, len)
                    progress += len
                    len = inStream.read(buffer)
                    listener?.downloading(length, progress)
                }
                //完成io操作,释放资源
                stream.flush()
                stream.close()
                inStream.close()
                listener?.done(file)
            } else {
                listener?.error(SocketTimeoutException("连接超时！"))
            }
            con.disconnect()
        } catch (e: Exception) {
            listener?.error(e)
            e.printStackTrace()
        }
    }

    override fun cancel() {
    }
}