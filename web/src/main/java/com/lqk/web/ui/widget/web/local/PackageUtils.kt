package com.lqk.web.ui.widget.web.local

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lqk.web.ui.activity.LocalPackageActivity
import com.lqk.web.ui.bean.VersionInfo
import com.lqk.web.utils.MMKVUtils
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
import kotlin.concurrent.thread

/**
 * @author LQK
 * @time 2022/2/23 9:27
 * @remark 离线包管理
 */
object PackageUtils {
    private const val TAG = "PackageUtils"

    // 本地缓存离线包目录
    private var packagesList = mutableListOf<VersionInfo>()
    const val KEY = "KEY_PACKAGE_INFO"


    /**
     * 读取本地离线包缓存目录 json -> list
     */
    private fun initLocalPackage(): MutableList<VersionInfo> {
        val jsonStr = MMKVUtils.gainString(KEY)
        if (jsonStr.isNotBlank()) {
            // 非空表示有数据
            val typeToken = object : TypeToken<MutableList<VersionInfo>>() {}.type
            val jsonObject =
                Gson().fromJson<MutableList<VersionInfo>>(jsonStr, typeToken)
            packagesList.clear()
            packagesList.addAll(jsonObject)
            packagesList.forEach {
                print("离线包: ${it.name}")
            }
        } else {
            packagesList = mutableListOf()
        }
        return packagesList
    }

    /**
     * 获取本地缓存目录
     */
    fun localPackage(): MutableList<VersionInfo> {
        if (packagesList.isEmpty()) {
            return initLocalPackage()
        }
        return packagesList
    }


    /**
     * 更新信息:
     * appId
     * home 启动入口
     * versionName 版本信息
     * 插入文件
     * @param versionInfo 离线包信息
     * 离线包保存到本地的路径 : cacheDir/web/
     * 打开 包首页 cacheDir/web/versionInfo.appId/versionInfo.home
     */
    private fun saveLocalPackage(versionInfo: VersionInfo) {
        // 判断是否是更新
        var insert = false
        for (v in packagesList) {
            if (v.appId == versionInfo.appId) {
                v.name = versionInfo.name
                v.home = versionInfo.home
                v.appId = versionInfo.appId
                v.category = versionInfo.category
                v.url = versionInfo.url
                v.versionName = versionInfo.versionName
                v.versionCode = versionInfo.versionCode
                insert = true
            }
        }
        if (!insert) {
            packagesList.add(versionInfo)
        }
        // 更新本地文件
        MMKVUtils.saveString(KEY, Gson().toJson(packagesList))
    }

    /**
     * 保存多个离线包信息
     */
    fun saveLocalPackage(versionInfo: MutableList<VersionInfo>) {
        // 筛选需要更新的 离线包信息
        packagesList.addAll(versionInfo)
        // 更新本地文件
        MMKVUtils.saveString(KEY, Gson().toJson(packagesList))
    }

    /**
     * 查询本地 package 是否存在
     * @see searchPackage
     */
    fun searchLocalPackage(versionInfo: VersionInfo): Boolean {
        return packagesList.search(versionInfo.appId) != null
    }

    /**
     * 下载前删除原有zip 文件
     */
    fun delZipPackage(versionInfo: VersionInfo, zipPath: String) {

    }

    /**
     * 解压 文件路径
     * @param zipPath
     */
    fun upZipPackage(versionInfo: VersionInfo, zipPath: String) {
        thread {
//            val zipPath = "$path/${versionInfo.name}/${versionInfo.appId}.zip"
            val file = File(zipPath)
//            Log.d(LocalPackageActivity.TAG, "downVersion: ${versionInfo.url}")
            // 压缩文件已经下载 -> 校验
            if (file.exists()) {
                Log.d(LocalPackageActivity.TAG, "doUnZipFile: 开始解压")

                val zipFile = ZipFile(file)
                val zipDir = file.absolutePath.substring(0, file.absolutePath.lastIndexOf('/'))

                Log.d(LocalPackageActivity.TAG, "doUnZipFile: $zipDir")
                Log.d(LocalPackageActivity.TAG, "doUnZipFile: ${zipFile.name}")

                val e = zipFile.entries()
                while (e.hasMoreElements()) {
                    val zipEntry = e.nextElement()
                    val entryName = zipEntry.name
                    // 文件路径
                    val path = "${zipDir}/$entryName"
                    var zipFileName = entryName.substring(0, entryName.lastIndexOf('/'))
                    Log.d(LocalPackageActivity.TAG, "doUnZipFile-: $path")

                    if (zipEntry.isDirectory) {
                        // 创建 文件夹
                        Log.d(LocalPackageActivity.TAG, "dir-doUnZipFile: $entryName")
                        val dirs = File(path)
                        if (!dirs.exists()) {
                            dirs.mkdirs()
                        }
                    } else {
                        // 文件
                        Log.d(LocalPackageActivity.TAG, "file-doUnZipFile: $entryName")
                        val mFileDir = File(zipDir)
                        if (!mFileDir.exists()) {
                            mFileDir.mkdirs()
                        }
                        val mFile = File("$zipDir/$entryName")

                        val bos = BufferedOutputStream(FileOutputStream(mFile))
                        val bi = BufferedInputStream(zipFile.getInputStream(zipEntry))
                        val readContent = ByteArray(1024)
                        var read = bi.read(readContent)
                        while (read != -1) {
                            bos.write(readContent, 0, read)
                            read = bi.read(readContent)
                        }
                        bos.close()
                    }
                }
                zipFile.close()
                // 解压完成 -> 保存到本地缓存目录(建立联系)
                saveLocalPackage(versionInfo)
            } else {
                Log.d(LocalPackageActivity.TAG, "doUnZipFile: 文件不存在请前往下载")
            }
        }.run()
    }

    /**
     * 对比本地版本 是否是最新的
     * @return true:最新-> 需要更新, false:不是最新 -> 不需要更新
     */
    fun checkPackageVersion(versionInfo: VersionInfo): Boolean {
        val oldVersion = initLocalPackage().search(versionInfo.appId)
        oldVersion ?: return true
        Log.d(TAG, "checkPackageVersion: ${versionInfo.versionName}:${oldVersion.versionName}")
        // 对比
        val oldArr = oldVersion.versionName.split('.').map { it.toInt() }
        val newArr = versionInfo.versionName.split('.').map { it.toInt() }
        if (newArr.size >= oldArr.size) {
            if (newArr.size == oldArr.size) {
                for (i in oldArr.indices) {
                    if (newArr[i] > oldArr[i]) {
                        Log.d(TAG, "checkPackageVersion: ${newArr[i]}:${oldArr[i]}")
                        return true
                    }
                }
                return false
            } else {
                return true
            }
        } else {
            return false
        }
    }

    /**
     * 扩展 根据包Id 查找包
     */
    fun MutableList<VersionInfo>.search(appId: String): VersionInfo? {
        forEach {
            if (it.appId == appId) {
                return it
            }
        }
        return null
    }

}