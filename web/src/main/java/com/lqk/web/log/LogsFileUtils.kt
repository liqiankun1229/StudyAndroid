package com.lqk.web.log

import android.app.Activity
import android.content.Context
import com.lqk.web.application.WebApplication
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author LQK
 * @time 2022/7/11 10:01
 * 日志文件的保存
 */
class LogsFileUtils {

    /**
     * 返回当前日期
     * yyyy-MM-dd
     */
    private fun getFileNameFromDate(): String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf.format(date)
    }

    /**
     * 获取当前时间 打印详情时间
     * yyyy-MM-dd HH:mm:ss SSS
     */
    private fun getTimeDate(): String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
        return sdf.format(date)
    }

    /**
     * 获取文件/创建文件
     */
    fun createFile(context: Context, fileName: String): File {
        // 文件目录
        val dir = context.filesDir
        val file = File("$dir/$fileName.log")
        if (!file.exists()) {
            // 文件不存在 创建文件
            file.createNewFile()
        }
        return file
    }

    /**
     * 格式化异常日志
     */
    private fun formatErrorLog() {
        // 分隔符
        // --------------------
        // 错误详情:
        // message
        // 错误堆栈:
        // xxx...
    }

    /**
     * 打印错误日志文件
     * time
     * 异常 描述
     * error: throwable.message
     * 异常 错误堆栈
     * errorStackTrace: throwable.stackTrace
     */
    fun addErrorLog(throwable: Throwable) {
        val errorFile = createFile(
            WebApplication.getApplication(),
            "${getFileNameFromDate()}-error"
        )

        // 写入日志
        throwable.message
        throwable.stackTrace

    }

    /**
     * 格式化 操作日志
     */
    private fun formatOperationsLog() {
        // --------------------
        // 页面:
        // activity 包地址 + 类名
        // 操作内容:
        // xxx
    }

    /**
     * 打印的 log
     * 页面
     * page: activity.packageName + activity.localClassName
     * 操作 描述
     * operations: 自定义的描述
     */
    fun addOperationsLog(activity: Activity, operations: String) {
        var logFile = File("${getFileNameFromDate()}-operations.log")
    }

    /**
     * 格式化 网络 日志
     */
    private fun formatNetworkLog() {
        // --------------------
        // 页面:
        // 请求地址:
        // xxx
        //
        // 请求体:
        // xxx
        //
        // 响应体:
        // xxx
    }

    /**
     * 打印的 log
     * 页面: activity.packageName + activity.localClassName
     * 请求接口:
     * 网络请求 请求内容/响应结果
     * 请求地址 URL:
     * xxx
     *
     * 请求头 Headers:
     * xxx
     *
     * 请求体 Request:
     * xxx
     *
     * 响应体 Response:
     * xxx
     */
    fun addNetworkLog(activity: Activity, url: URL) {
        var logFile = File("${getFileNameFromDate()}-network.log")
        activity.packageName + activity.localClassName
    }
}