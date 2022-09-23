package com.lqk.databinding

import androidx.databinding.ViewDataBinding
import com.lqk.BaseActivity

/**
 * @author LQK
 * @time 2022/8/6 23:03
 * 只使用 dataBinding 做框架实现
 */
abstract class BaseDBActivity<VDB : ViewDataBinding> : BaseActivity() {

    val viewDataBinding: VDB by lazy {
        initViewDataBinding()
    }

    abstract fun initViewDataBinding(): VDB

    override fun initActivity() {
        initWindow()
//        viewBinding = initViewBinding()
        setContentView(viewDataBinding.root)
        initView()
        initListener()
        initEvent()
        initData()
    }
}