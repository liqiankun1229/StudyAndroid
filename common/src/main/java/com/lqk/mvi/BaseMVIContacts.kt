package com.lqk.mvi

/**
 * @author LQK
 * @time 2022/2/26 11:00
 * @remark
 */

interface BaseMVIContacts {

    /**
     * 用户意图
     */
    interface IIntent {

    }

    /**
     * 数据状态
     */
    interface IState {}

    interface IView {
        // 发送意图
        fun send(intent: IIntent)

        // 接收状态 显示UI
        fun dispatchState(state: IState)
    }

    interface IModel {
        // 业务数据获取
        fun send(intent: IIntent)
    }
}

// 状态举例 一个意图对应一个返回状态
sealed class MainState : BaseMVIContacts.IState {

}