package com.lqk.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*

/**
 * @author LQK
 * @time 2019/9/11 16:42
 * @remark
 */
object KeybordUtil {

  fun showSoftInput(context: Context, editText: EditText) {
    Timer().schedule(object : TimerTask() {
      override fun run() {
        if (context is Activity) {
          context.runOnUiThread {
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
            val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(editText, 0)
          }
        }
      }
    }, 200)
  }

  fun closeKeybord(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
  }

  fun toggleKeybord(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isActive) {
      if (activity.currentFocus?.windowToken != null) {
        inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
      }
    }
  }
}