package com.lqk.butter.base

import androidx.viewbinding.ViewBinding
import com.lqk.butter.app.BaseApplication

/**
 * @author LQK
 * @time 2022/9/2 23:17
 *
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {
    protected lateinit var vb: VB
    abstract fun initViewBinding(): VB
    override fun initActivity() {
        BaseApplication.getInstance().addActivity(this)
        vb = initViewBinding()
        setContentView(layoutId())
        initView()
    }
}