package com.lqk.viewbinding

import androidx.viewbinding.ViewBinding
import com.lqk.BaseActivity

/**
 * @author LQK
 * @time 2022/8/1 15:43
 *
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    private val viewBinding: VB by lazy {
        initViewBinding()
    }

    abstract fun initViewBinding(): VB

    override fun initActivity() {
        initWindow()
//        viewBinding = initViewBinding()
        setContentView(viewBinding.root)
        initView()
        initListener()
        initEvent()
        initData()
    }
}