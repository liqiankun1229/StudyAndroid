package com.lqk.network

/**
 * @author LQK
 * @time 2019/4/15 8:52
 * @remark 网络请求结果
 */
interface NetWorkCallback<T> {
    /**
     * 成功 -> 返回数据
     */
    fun onSucceed(data: T)

    /**
     * 失败 -> 返回错误信息
     */
    fun onFailed(msg: String)
}