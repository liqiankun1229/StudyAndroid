package com.lqk.network.callback

/**
 * @author LQK
 * @time 2019/6/30 20:35
 * @remark
 */
interface IJsonDataCallback<T> {

    // 请求成功
    fun onSucceed(t: T)

    // 请求失败
    fun onFailed()

}