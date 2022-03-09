package com.lqk.utils

import android.content.Context

/**
 * @author LQK
 * @time 2019/7/15 22:04
 * @remark
 */
object DensityUtil {
    fun dp2px(context: Context, dp: Float): Int {
        // 获取设备密度
        val density = context.resources.displayMetrics.density
        // ＋0.5f 是为了四舍五入
        return (dp * density + 0.5f).toInt()
    }

    fun px2dp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }
}