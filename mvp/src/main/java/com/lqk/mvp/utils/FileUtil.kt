package com.lqk.mvp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * @author LQK
 * @time 2019/5/25 16:26
 * @remark 提供 文件处理
 ********* 文件 -> byte 数组
 */
object FileUtil {

    fun bitmapToByteArray(context: Context, resId: Int): ByteArray {
        val res = context.resources
        val bitmap = BitmapFactory.decodeResource(res, resId)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

}