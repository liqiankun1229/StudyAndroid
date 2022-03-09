package com.lqk.net

interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     */
    fun onSuccess(responseObj: Any)

    /**
     * 请求失败回调事件处理
     */
    fun onFailure(reasonObj: Any)
}
