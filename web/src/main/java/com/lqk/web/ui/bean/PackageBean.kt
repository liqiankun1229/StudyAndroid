package com.lqk.web.ui.bean

/**
 * @author LQK
 * @time 2022/2/24 11:32
 * @remark
 */


/**
 * @param category
 * @param url 下载地址
 * @param home 首页相对路径
 */
data class VersionInfo(
    var appId: String,
    var category: String,
    var name: String,
    var url: String,
    var versionCode: String,
    var versionName: String,
    var home: String,
    var packageType: Int,
    var onlineUrl: String
)

data class BaseResponse(
    val msg: String,
    val code: Int,
    var data: VersionInfo?
)

data class ResponsePackage(
    val msg: String,
    val code: Int,
    var data: MutableList<VersionInfo>?
)