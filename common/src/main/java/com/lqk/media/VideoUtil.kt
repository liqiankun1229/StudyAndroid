package com.lqk.media

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * @author LQK
 * @time 2019/3/8 11:10
 * @remark 视频播放组件
 * MediaPlayer 视频播放核心类
 * TextureView 显示帧数据核心类
 * {@link MediaPlayer}
 */
object VideoUtil {

    // 获取view的屏幕属性
    const val VIEW_INFO_EXTRA = "view_into_extra"
    const val PROPNAME_SCREENLOCATION_LEFT = "propname_sreenlocation_left"
    const val PROPNAME_SCREENLOCATION_TOP = "propname_sreenlocation_top"
    const val PROPNAME_WIDTH = "propname_width"
    const val PROPNAME_HEIGHT = "propname_height"

    fun dip2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale).toInt()
    }

    fun px2dip(context: Context, px: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (px / scale)
    }

    fun getVisiblePercent(pView: View?): Int {
        if (pView != null && pView.isShown) {
            val displayMetrics = pView.context.resources.displayMetrics
            val displayWidth = displayMetrics.widthPixels
            val rect = Rect()
            // 获取到当前播放器在 屏幕中的位置 矩形
            pView.getGlobalVisibleRect(rect)
            return if (rect.top > 0 && rect.left < displayWidth) {
                val areaVisible = rect.width() * rect.height()
                val areaTotal = (pView.width * pView.height).toDouble()
                (areaVisible / areaTotal * 100).toInt()
            } else {
                -1
            }
        }
        return -1
    }

    //is wifi connected
    fun isWifiConnected(context: Context): Boolean {
        if (context.checkCallingOrSelfPermission(permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    //decide can auto play the ad
    fun canAutoPlay(context: Context, setting: VideoConstant.AutoPlaySetting): Boolean {
        return when (setting) {
            VideoConstant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI -> {
                true
            }
            VideoConstant.AutoPlaySetting.AUTO_PLAY_ONLY_WIFI -> {
                isWifiConnected(context)
            }
            VideoConstant.AutoPlaySetting.AUTO_PLAY_NEVER -> {
                false
            }
        }
    }

    /**
     * 获取对应应用的版本号
     */
    fun getAppVersion(context: Context): String {
        var version = "1.0.0" //默认1.0.0版本
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            version = info.versionName
        } catch (e: Exception) {
        }

        return version
    }

    /**
     * 将数组中的所有素材IE拼接起来，空则拼接“”
     */
    fun getAdIE(values: ArrayList<ADValue>?): String {
        val result = StringBuilder()
        if (values != null && values.size > 0) {
            for (value in values) {
//                result.append(if (value.adid.equals("")) "" else value.adid).append(",")
            }
            return result.substring(0, result.length - 1)
        }
        return ""
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager ?: return displayMetrics
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    fun decodeImage(base64drawable: String): BitmapDrawable {
        val rawImageData = Base64.decode(base64drawable, 0)
        return BitmapDrawable(null, ByteArrayInputStream(rawImageData))
    }

    fun isPad(context: Context): Boolean {

        //如果能打电话那可能是平板或手机，再根据配置判断
        return if (canTelephone(context)) {
            //能打电话可能是手机也可能是平板
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        } else {
            true //不能打电话一定是平板
        }
    }

    private fun canTelephone(context: Context): Boolean {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephony.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    fun containString(source: String, destation: String): Boolean {

        if (source == "" || destation == "") {
            return false
        }

        return source.contains(destation)
    }


    fun getViewProperty(view: View): Bundle {
        val bundle = Bundle()
        val screenLocation = IntArray(2)
        view.getLocationOnScreen(screenLocation) //获取view在整个屏幕中的位置
        bundle.putInt(PROPNAME_SCREENLOCATION_LEFT, screenLocation[0])
        bundle.putInt(PROPNAME_SCREENLOCATION_TOP, screenLocation[1])
        bundle.putInt(PROPNAME_WIDTH, view.width)
        bundle.putInt(PROPNAME_HEIGHT, view.height)
        return bundle
    }

    /**
     * 把Bitmap保存成png文件
     *
     * @param dirPath 保存的目录
     * @param filename 保存的文件名
     * @param bitmap 要保存成文件的bitmap
     */
    fun saveBitmapToFile(dirPath: String, filename: String, bitmap: Bitmap?,
                         fm: Bitmap.CompressFormat?): Boolean {
        var format = fm
        if (TextUtils.isEmpty(dirPath) || TextUtils.isEmpty(filename) || bitmap == null) return false

        if (format == null) format = Bitmap.CompressFormat.PNG

        var out: FileOutputStream? = null
        try {
            val dir = File(dirPath)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return false
                }
            }

            val file = File(dirPath, filename)
            if (file.exists()) file.delete()

            out = FileOutputStream(file)
            if (bitmap.compress(format, 100, out)) {
                out.flush()
            } else {
                return false
            }
        } catch (e: IOException) {
            return false
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return true
    }
}