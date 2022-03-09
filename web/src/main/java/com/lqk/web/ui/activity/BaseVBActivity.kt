package com.lqk.web.ui.activity

import androidx.viewbinding.ViewBinding

/**
 * @author LQK
 * @date 2021/9/18
 * @remark viewBinding 基类
 */
abstract class BaseVBActivity<T : ViewBinding> : BaseActivity() {
    protected lateinit var viewBinding: T

    override fun initActivity() {
        viewBinding = initViewBinding()
        setContentView(viewBinding.root)
        initView()
        initEvent()
        initListener()
        initData()
    }

    abstract fun initViewBinding(): T
}
