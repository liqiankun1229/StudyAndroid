package com.lqk.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<T : ViewBinding> : BaseActivity() {

    protected lateinit var viewBinding: T

    override fun initActivity() {
        initWindow()
        viewBinding = initViewBinding()
        initVM()
        setContentView(viewBinding.root)
        initView()
        initEvent()
        initListener()
        initData()
    }

    abstract fun initViewBinding(): T

    /**
     * 关闭软键盘
     */
    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(
                    currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    override fun onStop() {
        closeKeyboard()
        super.onStop()
    }

    open fun initVM(){}

//    open fun checkPermission(vararg permissions: String): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkPermission(permissions) == PackageManager.PERMISSION_GRANTED
//        }
//        // 小于 6.0 不用请求权限
//        return true
//    }
}