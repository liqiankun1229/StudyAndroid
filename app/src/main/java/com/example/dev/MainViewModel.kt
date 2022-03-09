package com.example.dev

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author LQK
 * @date 2021/9/29
 * @remark
 */
class MainViewModel : ViewModel() {

    val nameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            loadData("123")
        }
    }

//    val myLiveData: LiveData<String> by lazy {
//        object : LiveData<String>().observe()
//    }


    fun loadData(name: String) {
        viewModelScope.launch {
            delay(2000)
            nameLiveData.value = name
        }
    }


    interface IDataChange {
        fun change(name: String)
    }


    override fun onCleared() {
        super.onCleared()
    }

}