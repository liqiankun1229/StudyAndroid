package com.lqk.mvp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * @author LQK
 * @time 2019/6/5 22:38
 * @remark
 */
object DeviceUtil {
    fun getIPAddress(context: Context): String {
        val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (networkInfo?.isConnected == true) {
            // 网络已连接
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                // 3G/4G 网络
                try {
                    var en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        var networkInterface = en.nextElement()
                        var enumerationAddress = networkInterface.inetAddresses
                        while (enumerationAddress.hasMoreElements()) {
                            var iNetAddress = enumerationAddress.nextElement()
                            if (!iNetAddress.isLoopbackAddress && iNetAddress is Inet4Address) {
                                return iNetAddress.hostAddress
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
            } else if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                // WiFi
                var wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                var wifiInfo = wifiManager.connectionInfo
                var ipAddress = intIP2StringIP(wifiInfo.ipAddress)
                return ipAddress
            } else if (networkInfo.type == ConnectivityManager.TYPE_ETHERNET) {
                return ""
            }
        }
        return ""
    }

    private fun intIP2StringIP(ip: Int): String {
        return "${ip and 0xFF}.${(ip shr 8) and 0xFF}.${(ip shr 16) and 0xFF}.${(ip shr 24) and 0xFF}"
    }
}