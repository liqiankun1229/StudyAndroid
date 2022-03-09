package com.lqk.activity.data

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.lqk.activity.R
import com.lqk.activity.bean.UpDateApk
import com.lqk.activity.ui.activity.MainActivity
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * @date 2018/9/18
 * @time 15:01
 * @remarks
 */
class DownloadService : Service() {

    private var downloadTask: DownloadTask? = null

    private var downloadUrl: String = ""

    private var strFilePath = ""

    private var listener = object : DownLoadListener {

        override fun onProgress(progress: Int) {

            getNotificationManager().notify(1, getNotification("Download...", progress))
        }

        override fun onSuccess() {
            downloadTask = null
            stopForeground(true)
            getNotificationManager().notify(1, getNotification("下载成功", -1))
            Toast.makeText(this@DownloadService, "-- onSuccess --", Toast.LENGTH_SHORT).show()
            EventBus.getDefault().post(UpDateApk())
        }

        override fun onFailed() {
            downloadTask = null
            stopForeground(true)
            getNotificationManager().notify(1, getNotification("下载失败", -1))
            Toast.makeText(this@DownloadService, "-- onFailed --", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            downloadTask = null
            Toast.makeText(this@DownloadService, "-- onPaused --", Toast.LENGTH_SHORT).show()
        }

        override fun onCanceled() {
            downloadTask = null
            stopForeground(true)
            Toast.makeText(this@DownloadService, "-- onCanceled --", Toast.LENGTH_SHORT).show()
        }

    }

    private var mBinder: DownloadBinder = DownloadBinder()

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    inner class DownloadBinder : Binder() {
        fun startDownload(url: String) {
            if (downloadTask == null) {
                downloadUrl = url
                downloadTask = DownloadTask(listener)
                downloadTask!!.execute(downloadUrl)
                startForeground(1, getNotification("下载中...", 0))

            }
        }

        fun pauseDownload() {
            if (downloadTask != null) {
                downloadTask!!.pauseDownload()
            }
        }

        fun cancelDownload() {
            if (downloadTask != null) {
                downloadTask!!.cancelDownload()
            }
            if (downloadUrl != "") {
                // 取消下载 删除已经下载有的文件
                val fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"))
                val directory = Environment.getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS).path
                val file = File("$directory$fileName")
                if (file.exists()) {
                    file.delete()
                }
                getNotificationManager().cancel(1)
                stopForeground(true)
                Toast.makeText(this@DownloadService, "-- Canceled --", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getNotificationManager(): NotificationManager {
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getNotification(title: String, progress: Int): Notification {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建通知渠道 只有在 Android 8.0 之后又这个概念
            //
            val channelId = "download"
            val channelName = "更新消息"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)

            val channelId2 = "downloading"
            val channelName2 = "更新下载进度"
            val importance2 = NotificationManager.IMPORTANCE_LOW
            createNotificationChannel(channelId2, channelName2, importance2)
        }

        val intent = Intent(this@DownloadService, MainActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "Test.apk")
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder: NotificationCompat.Builder = if (progress in 1..99) {
            NotificationCompat.Builder(this, "downloading")
        } else {
            NotificationCompat.Builder(this, "download")
        }
        builder.setSmallIcon(R.drawable.ic_launcher_background)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
        builder.setContentIntent(pendingIntent)
        builder.setContentTitle(title)
        if (progress >= 0) {
            builder.setContentText("$progress%")
            builder.setProgress(100, progress, false)
            builder.setSound(null)
        }
        return builder.build()
    }

    /**
     * 创建通知渠道
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        // 角标功能，查看消息需要长按应用图标
        channel.setShowBadge(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}