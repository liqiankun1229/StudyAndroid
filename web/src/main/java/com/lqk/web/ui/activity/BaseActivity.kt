package com.lqk.web.ui.activity

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * @author LQK
 * @date 2021/9/18
 * @remark 普通 Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
        var isGray: Boolean = false
    }

    lateinit var pageUUID: String
    var isResetBrightness = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("start_time", "attachBaseContext: ${System.currentTimeMillis()}")
        pageUUID = UUID.randomUUID().toString()
        initWindow()
        initActivity()
        initGrayTheme()
    }

    //实现一键置灰功能
    private fun initGrayTheme() {
        if (isGray) {
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
        }
    }

    open fun initActivity() {
        setContentView(initLayoutId())
        initView()
        initEvent()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: $isResetBrightness")
        // 重置亮度
        if (isResetBrightness) {
            isResetBrightness = false
        }
    }

    open fun initWindow() {}

    /**
     * 子类必须复写 获取页面 layout
     */
    abstract fun initLayoutId(): Int

    /**
     * 页面点击..事件监听
     */
    open fun initListener() {}

    /**
     * 页面事件监听
     */
    open fun initEvent() {

    }

    /**
     * 页面 view 设置
     */
    open fun initView() {}

    /**
     * 获取页面数据
     */
    open fun initData() {}

    //<editor-fold desc="输入软键盘的显示和隐藏">
    protected fun toggleInput() {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    protected fun showInput(editText: EditText) {
        editText.requestFocus()
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    protected fun hideInput(editText: EditText) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
    //</editor-fold>
}