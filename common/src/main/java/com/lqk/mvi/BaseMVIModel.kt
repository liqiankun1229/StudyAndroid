package com.lqk.mvi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author LQK
 * @time 2022/2/26 11:00
 * @remark 请求数据
 */
abstract class BaseMVIModel<Data : BaseMVIContacts.IState>(view: BaseMVIContacts.IView) :
    ViewModel(), BaseMVIContacts.IModel {

    protected val viewModel: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>()
    }

    protected val mView = view

    override fun send(intent: BaseMVIContacts.IIntent) {
        // 分析意图

    }


}