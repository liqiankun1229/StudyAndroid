package com.lqk.web.ext

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * @author LQK
 * @time 2022/2/24 16:39
 * @remark
 */

fun Context.showDialog(
    title: String,
    desc: String,
    cancelTxt: String,
    confirmTxt: String,
    cancel: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(desc)
        .setNegativeButton(cancelTxt) { dialog, _ ->
            kotlin.run {
                cancel()
                dialog.dismiss()
            }
        }
        .setNeutralButton(confirmTxt) { dialog, _ ->
            kotlin.run {
                confirm()
                dialog.dismiss()
            }
        }.show()
}