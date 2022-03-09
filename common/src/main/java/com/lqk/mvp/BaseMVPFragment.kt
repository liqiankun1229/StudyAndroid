package com.lqk.mvp

import com.lqk.base.BaseFragment

/**
 * @author LQK
 * @time 2019/9/25 22:29
 * @remark
 */
abstract class BaseMVPFragment<P : BaseMVPPresenter<*>> : BaseFragment() {
    // 子类直接使用
    protected lateinit var mPresenter: P

    // 子类构建 Presenter 对象
    abstract fun initPresenter(): P

    override fun initView() {
        super.initView()
        // 赋值
        mPresenter = initPresenter()
    }
}