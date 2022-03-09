package com.lqk.web.ui.widget.web.local

import android.content.Context
import android.util.Log
import com.lqk.web.ui.bean.VersionInfo
import java.io.File

/**
 * @author LQK
 * @time 2022/2/24 15:03
 * @remark
 */

/**
 * 本地离线包缓存目录
 * 先检测, 没有该文件夹就创建
 */
fun Context.cachePackage(): String {
    val file = File("${cacheDir.absolutePath}/web")
    if (!file.exists()) file.mkdirs()
    return "${cacheDir.absolutePath}/web"
}

/**
 * 本地离线包缓存目录
 * 先检测, 没有该文件夹就创建
 * 检测 zip 离线包是否存在 跳过 直接判断入口文件
 * 检测 zip 离线包模块入口文件 cacheDir/web/versionInfo.appId/versionInfo.home 是否存在
 */
fun Context.searchPackage(versionInfo: VersionInfo): Boolean {
    val file = File("${cacheDir.absolutePath}/web")
    return if (!file.exists()) {
        file.mkdirs()
        false
    } else {
        //  入口文件
        Log.d(
            "PackageInfo",
            "searchPackage: ${file.absolutePath}/${versionInfo.appId}/${versionInfo.home}"
        )
        val home = File("${file.absolutePath}/${versionInfo.appId}/${versionInfo.home}")
        home.exists()
    }

}