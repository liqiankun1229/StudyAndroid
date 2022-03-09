package com.lqk.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.lqk.R

/**
 * @author LQK
 * @time 2019/6/23 18:59
 * @remark
 */
object DialogUtil {
  /**
   * 对话框类型
   * 单个按钮 (确认 / 取消)
   * 两个按钮 (确认-取消 / 是-否)
   * 三个按钮 (是-帮助-否)
   */
  enum class DialogType {
    One,
    Two,
    Three
  }

  /**
   * 显示对话框 信息提示
   */
  fun showMessageDialog(context: Context, view: View, title: String, type: DialogType) {
    when (type) {
      DialogUtil.DialogType.One -> {
      }
      DialogUtil.DialogType.Two -> {
      }
      DialogUtil.DialogType.Three -> {
      }
    }
  }

  fun showSingleDialog(context: Context, viewId: Int, title: String, msg: String, btnMsg: String) {
    var rootView = LayoutInflater.from(context).inflate(viewId, null)
    rootView.findViewById<TextView>(R.id.tv_dialog_title).text = title
    rootView.findViewById<TextView>(R.id.tv_dialog_title).text = title
    rootView.findViewById<TextView>(R.id.tv_dialog_title).text = title
    var customDialog = AlertDialog.Builder(context)
    customDialog.setView(rootView)
  }

}