package com.lqk.activity.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

/**
 * @date 2018/9/18
 * @time 9:30
 * @remarks
 */
object ApkUtil {

    /**
     * 调起 安装 APP 窗口 安装 APP
     */
    fun installApk(context: Context, strApkFile: String) {
//        val fileLocation = File(Constacts.apkFilePath, Constacts.apkName)
        val fileLocation = File(strApkFile)
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.fromFile(fileLocation)
//        intent.addCategory(Intent.CATEGORY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri = FileProvider.getUriForFile(
                    context,
                    "com.lqk.activitydemo.provider",
                    fileLocation)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.android.package-archive")
        }
    }

    fun setApkPermission(filePath: String) {
        val command = "chmod 777 $filePath"
        val runtime = Runtime.getRuntime()
        try {
            runtime.exec(command)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}