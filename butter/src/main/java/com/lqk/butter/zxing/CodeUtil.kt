package com.lqk.butter.zxing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.lqk.butter.zxing.core.BarcodeFormat
import com.lqk.butter.zxing.core.MultiFormatWriter
import com.lqk.butter.zxing.core.WriterException
import com.lqk.butter.utils.DensityUtil
import com.lqk.butter.zxing.core.EncodeHintType
import java.util.*

/**
 * @author LQK
 * @time 2019/3/14 16:58
 * @remark 二维码相关
 * String -> 图
 */
object CodeUtil {
    // 默认宽度
    const val IMAGE_WIDTH = 20

    @Throws(WriterException::class)
    fun createCode(context: Context, content: String): Bitmap {

        val hints = Hashtable<EncodeHintType, String>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        val matrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
                DensityUtil.dp2px(context, 250F),
                DensityUtil.dp2px(context, 250F),
                hints)
        val width = matrix.width
        val height = matrix.height
        // 将二维矩阵转换成一维像素数组
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000.toInt()
                } else {
                    pixels[y + width + x] = Color.WHITE
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }


}