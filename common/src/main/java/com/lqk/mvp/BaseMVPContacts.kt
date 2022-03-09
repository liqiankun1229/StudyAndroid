package com.lqk.mvp

import androidx.annotation.UiThread

/**
 * @author LQK
 * @time 2019/9/25 21:19
 * @remark
 */
interface BaseMVPContacts {

    /**
     * 页面
     */
    interface IView {}

    /**
     * 业务逻辑, 持有 view
     */
    interface IPresenter<V : IView> {
        @UiThread
        fun attachView(view: V)

        @UiThread
        fun destroyView()

        @UiThread
        fun destroy()
    }

    /**
     * 数据的获取
     */
    interface IModel {}

}