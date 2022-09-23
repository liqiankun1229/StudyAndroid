package com.example.dev.bean

import java.io.Serializable

/**
 * @author LQK
 * @time 2022/8/1 10:42
 *
 */
data class PackageBean(
    var appId: String,
    var category: String,
    var name: String,
    var url: String,
    var versionCode: String,
    var versionName: String,
    var home: String,
    var packageType: Int,
    var onlineUrl: String
) : Serializable