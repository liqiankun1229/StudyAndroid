package com.lqk.activity.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.lang.ref.WeakReference

/**
 * @author LQK
 * @time 2018/12/2 23:04
 * @remark
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpdateApkUtil {
    companion object {
        const val TAG = "AppDownloadManager"

        //通过downLoadId查询下载的apk，解决6.0以后安装的问题
        fun queryDownloadedApk(context: Context, downloadId: Long): File? {
            var targetApkFile: File? = null
            val downloader = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            if (downloadId != -1L) {
                val query = DownloadManager.Query()
                query.setFilterById(downloadId);
                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
                val cur = downloader.query(query)
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        var uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(uriString)) {
                            targetApkFile = File(Uri.parse(uriString).path)
                        }
                    }
                    cur.close()
                }
            }
            return targetApkFile
        }

    }

    private lateinit var weakReference: WeakReference<Activity>
    private lateinit var mDownloadManager: DownloadManager
    private var mDownLoadChangeObserver: DownloadChangeObserver? = null
    private lateinit var mDownloadReceiver: DownloadReceiver
    private var mReqId: Long = -1
    private var mUpdateListener: OnUpdateListener? = null

    constructor(activity: Activity) {
        weakReference = WeakReference(activity)
        mDownloadManager = weakReference.get()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        mDownLoadChangeObserver = DownloadChangeObserver(Handler())
        mDownloadReceiver = DownloadReceiver()
    }

    fun setUpdateListener(mUpdateListener: OnUpdateListener) {
        this.mUpdateListener = mUpdateListener
    }

    fun downloadApk(apkUrl: String, title: String, desc: String) {
        // fix bug : 装不了新版本，在下载之前应该删除已有文件
        val apkFile = File(weakReference.get()?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app_name.apk")

        if (apkFile.exists()) {
            apkFile.delete()
        }
        val request = DownloadManager.Request(Uri.parse(apkUrl));
        //设置title
        request.setTitle(title)
        // 设置描述
        request.setDescription(desc)
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS, "app_name.apk")
        //在手机SD卡上创建一个download文件夹
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir()
        //指定下载到SD卡的/download/my/目录下
        // request.setDestinationInExternalPublicDir("/codoon/","codoon_health.apk")

        request.setMimeType("application/vnd.android.package-archive")
        //
        mReqId = mDownloadManager.enqueue(request)
    }

    /**
     * 取消下载
     */
    fun cancel() {
        mDownloadManager.remove(mReqId)
    }

    /**
     * 对应 {@link Activity }
     */
    fun resume() {
        //设置监听Uri.parse("content://downloads/my_downloads")
        weakReference.get()?.contentResolver?.registerContentObserver(Uri.parse("content://downloads/my_downloads"),
                true,
                mDownLoadChangeObserver as ContentObserver)
        // 注册广播，监听APK是否下载完成
        weakReference.get()?.registerReceiver(mDownloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    /**
     * 对应{@link Activity#onPause()} ()}
     */
    fun onPause() {
        //
//        weakReference.get()?.contentResolver?.unregisterContentObserver(mDownLoadChangeObserver)
//        weakReference.get()?.unregisterReceiver(mDownloadReceiver)
    }

    private fun updateView() {
        val bytesAndStatus = IntArray(3)
        val query = DownloadManager.Query().setFilterById(mReqId)
        var c: Cursor? = null
        try {
            c = mDownloadManager.query(query)
            if (c != null && c.moveToFirst()) {
                // 已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                // 总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                // 状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }
        } finally {
            c?.close()
        }
        if (mUpdateListener != null) {
            mUpdateListener?.update(bytesAndStatus[0], bytesAndStatus[1])
        }
        Log.i(TAG, "下载进度：" + bytesAndStatus[0] + "/" + bytesAndStatus[1] + "")
    }

    /**
     * 下载进度通知
     */
    inner class DownloadChangeObserver(handler: Handler) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            updateView()
        }
    }

    inner class DownloadReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            if (context == null || intent == null) {
                return
            }
            var haveInstallPermission = false
            // 兼容Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                //先获取是否有安装未知来源应用的权限
                haveInstallPermission = context.packageManager.canRequestPackageInstalls()
                if (!haveInstallPermission) {//没有权限
                    // 弹窗，并去设置页面授权
                    val listener = object : AndroidOInstallPermissionListener {

                        override fun permissionSuccess() {
                            installApk(context, intent)
                        }

                        override fun permissionFail() {
                            Toast.makeText(context, "授权失败，无法安装应用", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // android 8.0 申请 安装权限
//                    AndroidOPermissionActivity.sListener = listener
//                    var intent1 = Intent(context, AndroidOPermissionActivity.class)
//                    context!!.startActivity(intent1)
                } else {
                    installApk(context, intent)
                }
            } else {
                installApk(context, intent)
            }
        }
    }

    private fun installApk(context: Context, intent: Intent) {
        val completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val uri: Uri?
        val intentInstall = Intent()
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intentInstall.action = Intent.ACTION_VIEW

        if (completeDownLoadId == mReqId) {
            when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                    // 6.0以下
                    uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId)
                }
                Build.VERSION.SDK_INT < Build.VERSION_CODES.N -> {
                    // 6.0 - 7.0
                    val apkFile = queryDownloadedApk(context, completeDownLoadId)
                    uri = Uri.fromFile(apkFile)
                }
                else -> {
                    // Android 7.0 以上
                    uri = FileProvider.getUriForFile(context,
                            "package_name.fileProvider",
                            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app_name.apk"))
                    intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
            }
            // 安装应用
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intentInstall)
        }
    }

    /**
     * 下载进度监听
     */
    interface OnUpdateListener {
        fun update(currentByte: Int, totalByte: Int)
    }

    /**
     * Android 8.0 申请权限监听
     */
    interface AndroidOInstallPermissionListener {
        fun permissionSuccess()

        fun permissionFail()
    }
}