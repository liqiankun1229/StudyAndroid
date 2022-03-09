package com.lqk.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * te
 * <img width="640" height="308" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/observeOn.png" alt="">
 * et
 *
 * @author LQK
 * @time 2022/2/26 20:43
 * @remark 数据
 *
 *
 */
class BaseMVVMViewModel<Data> : ViewModel() {
    val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    fun loadData(): LiveData<Data>? {

        return data
    }

    fun upload(){
        viewModelScope.launch {
            // 加载数据
            suspend {  }
        }
    }

}