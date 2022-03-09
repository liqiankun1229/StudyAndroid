package com.lqk.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

/**
 * @author LQK
 * @time 2019/6/29 15:19
 * @remark
 */
object Base64Util {
  @RequiresApi(Build.VERSION_CODES.O)
  fun encodeBase64(data: ByteArray): String {
    return Base64.getEncoder().encodeToString(data)
  }
}