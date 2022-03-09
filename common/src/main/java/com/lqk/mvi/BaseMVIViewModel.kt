package com.lqk.mvi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author LQK
 * @time 2022/2/26 10:20
 * @remark 数据源
 */
class BaseMVIViewModel<T : BaseMVIContacts.IState> : ViewModel() {
    private val liveData: MutableLiveData<T> by lazy {
        MutableLiveData<T>().also {
            loadData()
        }
    }

    /**
     * 请求数据
     * 响应意图
     * 返回 State
     */
    fun loadData(): T? {

        return liveData.value
    }
}