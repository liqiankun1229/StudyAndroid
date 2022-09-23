package com.example.dev.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dev.bean.PackageBean
import com.example.dev.net.APIService
import com.example.dev.net.RetrofitHelper
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author LQK
 * @date 2021/9/29
 * @remark
 */
class MainViewModel : ViewModel() {


    private val _packageList = MutableLiveData<MutableList<PackageBean>>()

    // 保证外部只能观察, 不能修改
    val pList: LiveData<MutableList<PackageBean>> = _packageList

    private val _errorMsg = MutableLiveData<String>()

    // 提供外部观察
    val errorMsg: LiveData<String> = _errorMsg

    fun upData() {
        viewModelScope.launch {
            val result =
                RetrofitHelper.instance.getService(APIService::class.java)
                    .packageList()
            _packageList.value = result.data
        }
    }

    fun upData2() {
        viewModelScope.launch {
            try {
                val result =
                    RetrofitHelper.instance.getService(APIService::class.java)
                        .packageList()
                // 请求成功
                _packageList.value = result.data

            } catch (e: Throwable) {
                val msg = when (e) {
                    is CancellationException -> {
                        // 请求被关闭
                        "请求被关闭"
                    }
                    is SocketTimeoutException -> {
                        // 链接超时
                        "链接超时"
                    }
                    is JsonParseException -> {
                        // 数据解析失败
                        "数据解析失败"
                    }
                    is HttpException -> {
                        when(e.code()){
                            405 -> {
                                "链接不存在"
                            }
                            else ->{
                                "未知错误"
                            }
                        }
                    }
                    else -> {
                        // 未知错误
                        "未知错误"
                    }
                }
                Log.d("error", "upData2: ${e.message}")
                Log.d("error", "upData2: ${e.cause}")

                _errorMsg.value = msg
            } finally {
                //
            }
        }
    }
}