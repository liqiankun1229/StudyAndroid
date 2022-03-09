package com.lqk.activity.ui.activity

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.lqk.activity.R
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * 通知栏消息
 */
class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建通知渠道 只有在 Android 8.0 之后又这个概念
            //
            var channelId = "chat"
            var channelName = "聊天消息"
            var importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)

            channelId = "system"
            channelName = "系统消息"
            importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)

        }

        btn_notification.setOnClickListener {
            // 先判断用户是否关闭了通知
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = notificationManager.getNotificationChannel("chat")
                if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                    startActivity(intent)
                }
            }
            val notificationIntent = Intent(this, RegisterActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            // 通过 NotificationCompat 构建通知
            val notification = NotificationCompat.Builder(this, "chat")
                    .setContentTitle("聊天通知")
                    .setContentText("您有新的聊天消息")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.advice)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
                    .setAutoCancel(true)
                    .setNumber(3) // 消息数
                    .setContentIntent(pendingIntent) // 设置点击跳转界面
                    .setLights(Color.RED, 1000, 1000) //设置前置指示灯
                    //                    .setSound(Uri.form(File(""))) // 播放声音文件
                    .setVibrate(longArrayOf(0, 1000, 1000, 1000))
                    .build()
            notificationManager.notify(1, notification)
        }

        btn_subscribe.setOnClickListener {
            val notification = NotificationCompat.Builder(this, "system")
                    .setContentTitle("系统通知")
                    .setContentText("电量不足")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.advice)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
                    .setAutoCancel(true)
                    .setNumber(2)
                    .build()
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2, notification)
        }

        btn_del_system.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deleteNotificationChannel("system")
            }
        }
    }

    fun createNotification(msg: String) {
        var notification = NotificationCompat.Builder(this, "")
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

    /**
     * 删除通知渠道
     *
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun deleteNotificationChannel(channelId: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(channelId)
    }
}
