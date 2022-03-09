package com.lqk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.telephony.TelephonyManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*

/**
 * @author LQK
 * @time 2019/5/20 20:18
 * @remark Android 设备信息获取
 */
object SystemUtil {

  const val IP_ERROR = "0.0.0.0"

  /**
   * 获取设备宽度
   */
  fun deviceWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
  }

  /**
   * 获取设备高度
   */
  fun deviceHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
  }


  /**
   * 获取当前系统语言
   */
  fun systemLanguage(): String {
    return Locale.getDefault().language
  }

  /**
   * 获取当前系统语言列表
   */
  fun systemLanguageList(): Array<Locale> {
    return Locale.getAvailableLocales()
  }

  /**
   * 获取当前手机系统版本号
   */
  fun systemVersion(): String {
    return android.os.Build.VERSION.RELEASE
  }

  /**
   * 获取手机厂商
   */
  fun deviceBrand(): String {
    return android.os.Build.BRAND
  }

  /**
   * 获取产品名
   */
  fun deviceProduct(): String {
    return android.os.Build.PRODUCT
  }

  /**
   * 获取手机型号
   */
  fun deviceModel(): String {
    return android.os.Build.MODEL
  }

  /**
   * 获取手机主板名
   */
  fun deviceBoard(): String {
    return android.os.Build.BOARD
  }

  /**
   * 获取设备名
   */
  fun deviceName(): String {
    return android.os.Build.DEVICE
  }

  /**
   * 获取手机 IMEI 手机序列号
   * 需要权限 android.permission.READ_PHONE_STATE
   */
  @SuppressLint("MissingPermission", "HardwareIds")
  fun getIMEI(context: Context): String {
    val tm = context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
    return tm.deviceId
  }

  /**
   * 获取手机ip 地址
   * todo
   */
  @SuppressLint("WifiManagerPotentialLeak")
  fun deviceIpAddress(context: Context): String {
    return ""
  }

  private fun intIP2StringIP(ip: Int): String {
    return "${(ip and 0xFF)}.${((ip shr 8) and 0xFF)}.${((ip shr 16) and 0xFF)}.${(ip shr 24 and 0xFF)}"
  }

  /**
   * 有限网络
   */
  private fun localIP(): String {
    try {
      for (en in NetworkInterface.getNetworkInterfaces()) {
        for (ipAddress in en.inetAddresses) {
          if (!ipAddress.isLoopbackAddress && ipAddress is Inet4Address) {
            return ipAddress.hostAddress
          }
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      return IP_ERROR
    }
    return IP_ERROR
  }

}