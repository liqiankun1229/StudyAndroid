package com.example.dev.net

import java.io.Serializable

/**
 * @author LQK
 * @time 2022/8/1 10:40
 * 网络请求基类
 */
data class BaseResponseBean<T>(
    var code: Int,
    var msg: String,
    var data: T
) : Serializable