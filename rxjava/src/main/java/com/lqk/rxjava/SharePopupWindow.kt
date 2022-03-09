package com.lqk.rxjava

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast

/**
 * @author LQK
 * @time 2019/7/15 21:02
 * @remark
 */
class SharePopupWindow : PopupWindow {

  private var context: Context? = null

  constructor(layoutId: Int, context: Context) : super(context) {
    this.context = context
    initView(layoutId)
  }

  private fun initView(layoutId: Int) {
    val rootView = LayoutInflater.from(context).inflate(layoutId, null)
    rootView.findViewById<TextView>(R.id.tv_qq).setOnClickListener {
      Toast.makeText(context, "QQ", Toast.LENGTH_SHORT).show()
    }
    rootView.findViewById<TextView>(R.id.tv_wx).setOnClickListener {
      Toast.makeText(context, "微信", Toast.LENGTH_SHORT).show()
    }


    this.contentView = rootView
    this.width = ViewGroup.LayoutParams.MATCH_PARENT
    this.height = ViewGroup.LayoutParams.WRAP_CONTENT
    this.isFocusable = true

    this.animationStyle = R.style.SharePopupWindow
    this.setBackgroundDrawable(ColorDrawable(0x0f000000))

  }
}