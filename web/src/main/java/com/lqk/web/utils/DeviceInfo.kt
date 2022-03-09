package com.lqk.web.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager

/**
 * @author LQK
 * @time 2022/2/14 10:10
 * @remark 设备信息
 */
object DeviceInfo {
    fun netWorkInfo(): String {
        return ""
    }
}


/**
 * 判断是否链接移动网络
 */
fun Context.isMobileDataEnabled(): Boolean {


    return false
}

/**
 * 判断是否插入 SIM 卡
 */
fun Context.hasSIM(): Boolean {
    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.simOperator != ""
}

/**
 * 获取运营商名称
 */
fun Application.getNetworkOperatorName(): String {

    if (!hasSIM()) {
        return "unknown"
    }
    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return when (tm.simOperator) {
        "46001", "46006", "46009" -> {
            "中国联通"
        }
        "46000", "46002", "46004", "46007" -> {
            "中国移动"
        }
        "46003", "46005", "46011" -> {
            "中国电信"
        }
        else -> {
            "unknown"
        }
    }
}

/**
 * 获取 网络状态
 * 2G 3G 4G 5G WiFi NONE UNKNOWN
 */
fun Context.getNetworkState(): String {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netWork = connectivityManager.allNetworks
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var activte = connectivityManager.activeNetwork
        activte?.let {
//            if (it.)
        } ?: return "NONE"
    } else {

    }
    if (netWork.isNotEmpty()) {

    } else {
        return "NONE"
    }
    return "NONE"
}

/**
 * 网络状态
 * wifi 不影响
 * 流量 开: true 关 false
 */
@SuppressLint("DiscouragedPrivateApi")
fun Context.getNetWorkInfo(): Boolean {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        try {
            val method = ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            val connectivityManager =
                getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            val s = method.invoke(connectivityManager)?.toString()
            s == "true"
        } catch (e: Exception) {
            false
        }
    } else {
        false
    }
}