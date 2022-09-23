package com.lqk.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author LQK
 * @time 2022/8/3 17:07
 *
 */
open class BaseViewModel : ViewModel() {

    // 定义 视图状态
    private val _viewStates: MutableLiveData<BaseViewState> = MutableLiveData()
    val viewStates: LiveData<BaseViewState> = _viewStates

    private val _viewEvents: MutableLiveData<String> = MutableLiveData()
    val viewEvents: LiveData<String> = _viewEvents


    fun emit(state: BaseViewState) {

    }


    fun dispatch(action: BaseAction) {

    }
}