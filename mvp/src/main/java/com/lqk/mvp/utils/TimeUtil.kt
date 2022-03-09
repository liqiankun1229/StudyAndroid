package com.lqk.mvp.utils

/**
 * @author LQK
 * @time 2019/5/25 11:08
 * @remark 时间工具类
 */
object TimeUtil {

    /**
     * 给当前文本加上时间
     */
    fun addStringTime(text: String): String {
        return "$text ${System.currentTimeMillis()}"
    }

    /**
     * 获取字符串格式的系统时间
     */
    fun systemTimeString(): String {
        return "${System.currentTimeMillis()}"
    }

}