package com.lqk.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.Serializable

/**
 * @author LQK
 * @time 2022/8/1 17:18
 *
 */
abstract class BaseViewModel<Data : Serializable> : ViewModel() {
    // 数据源
    private val _baseLiveData: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>()
    }

    // 数据源被外部监听
    val baseLiveData: LiveData<Data> = _baseLiveData

    fun loadData(): LiveData<Data> {
        return MutableLiveData<Data>()
    }

    private val _baseMutableLiveData: MutableList<MutableLiveData<Data>> = mutableListOf()

    fun addLiveData(mutableLiveData: MutableLiveData<Data>) {
        _baseMutableLiveData.add(mutableLiveData)
        var s:LiveData<Data> = mutableLiveData
    }

    /**
     * 异常执行
     */
    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }
}