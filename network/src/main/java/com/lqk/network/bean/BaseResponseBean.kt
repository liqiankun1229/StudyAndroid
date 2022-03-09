package com.lqk.network.bean

import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * @author LQK
 * @time 2019/4/28 11:38
 * @remark 网络请求数据基类
 */
open class BaseResponseBean<T> : Observer<T> {
    var code: Int = 0
    var msg: String = ""
    var data: T? = null
    @get:JvmName("data")
    var jvmData = ""

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        // 数据处理
        onSucceed(t)
    }

    override fun onError(e: Throwable) {
        onFailed(e.message ?: "网络错误")
    }

    open fun onSucceed(data: T) {

    }

    open fun onFailed(msg: String) {}
}