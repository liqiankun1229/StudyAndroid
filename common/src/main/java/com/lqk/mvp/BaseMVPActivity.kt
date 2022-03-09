package com.lqk.mvp

import com.lqk.base.BaseActivity

/**
 * @author LQK
 * @time 2019/9/25 21:18
 * @remark
 */
abstract class BaseMVPActivity<P : BaseMVPPresenter<*>> : BaseActivity() {
    // 子类直接使用
    protected lateinit var mPresenter: P

    // 子类构建 Presenter
    abstract fun initPresenter(): P

    override fun initView() {
        super.initView()
        mPresenter = initPresenter()
    }
}
