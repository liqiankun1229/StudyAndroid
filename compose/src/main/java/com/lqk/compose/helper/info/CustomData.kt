package com.lqk.compose.helper.info

import android.graphics.drawable.Drawable

/**
KT 数据
 * 包名
 * 应用名称
 * ICON
 * 第一启动类
 * 是否系统应用
 */
data class AppData(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable,
    val firstRunName: String,
    val isSystemApp: Boolean
)