package com.lqk.rxjava

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.lqk.utils.DensityUtil


/**
 * @author LQK
 * @time 2019/7/15 21:41
 * @remark
 */
object ShareBottomDialog {
  fun show(context: Context) {
    val bottomDialog = Dialog(context, R.style.BottomDialog)
    val contentView = LayoutInflater.from(context).inflate(R.layout.layout_share, null)
    contentView.findViewById<TextView>(R.id.tv_qq).setOnClickListener {
      Toast.makeText(context, "QQ", Toast.LENGTH_SHORT).show()
      bottomDialog.dismiss()
    }
    contentView.findViewById<TextView>(R.id.tv_wx).setOnClickListener {
      Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show()
      bottomDialog.dismiss()
    }

    bottomDialog.setContentView(contentView)
    val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
    params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16F)
    params.bottomMargin = DensityUtil.dp2px(context, 16F)
    contentView.setLayoutParams(params)
    bottomDialog.window?.setGravity(Gravity.BOTTOM)
    bottomDialog.window?.setWindowAnimations(R.style.SharePopupWindow)
    bottomDialog.show()
  }
}