package com.lqk.network

import retrofit2.Converter

/**
 * @author LQK
 * @time 2019/4/15 14:36
 * @remark
 */
class NetWorkResponse<T> : Converter<T, T> {
    var code: Int = 200
    var msg: String = ""
    var data: T? = null


    override fun convert(value: T): T? {
        return data
    }
}