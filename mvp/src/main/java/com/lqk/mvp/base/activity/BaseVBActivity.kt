package com.lqk.mvp.base.activity

import androidx.viewbinding.ViewBinding

/**
 * @author LQK
 * @time 2022/9/2 17:02
 *
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    protected lateinit var vb: VB
    abstract fun initVB(): VB
    override fun initView() {
    }

    override fun initActivity() {
        vb = initVB()
        setContentView(vb.root)
        initView()
        initCustom()
    }

    open fun initCustom() {

    }
}