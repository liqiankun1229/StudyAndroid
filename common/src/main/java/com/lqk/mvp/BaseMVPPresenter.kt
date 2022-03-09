package com.lqk.mvp

import androidx.annotation.UiThread

/**
 * @author LQK
 * @time 2019/9/28 16:39
 * @remark
 */
abstract class BaseMVPPresenter<V : BaseMVPContacts.IView>(view: V? = null) :
    BaseMVPContacts.IPresenter<V> {

    // 持有 View
    private var mView: V? = view

    @UiThread
    override fun attachView(view: V) {
        this.mView = view
    }

    @UiThread
    override fun destroyView() {
        this.mView = null
    }

    override fun destroy() {
        // 销毁
    }

    // Model 获取数据
    // 具体由 用户自定义业务场景


}