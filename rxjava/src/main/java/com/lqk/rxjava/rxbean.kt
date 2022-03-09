package com.lqk.rxjava

/**
 * @author LQK
 * @time 2022/3/1 23:32
 * @remark
 */

// 观察者
interface CustomObserver<T> {
    fun changeValue(t: T)
}

// 被观察者
class CustomObservable<T>(t: T) {
    // 数据
    private var t: T = t

    // 观察者列表
    private var observerList: MutableList<CustomObserver<T>> = mutableListOf()

    // 数据变化
    fun changeValue(t: T) {
        this.t = t
        notifyObserver()
    }

    // 数据刷新通知观察者
    private fun notifyObserver() {
        for (customObserver in observerList) {
            customObserver.changeValue(this.t)
        }
    }

    // 添加观察者
    fun addObserver(observer: CustomObserver<T>): CustomObservable<T> {
        this.observerList.add(observer)
        return this
    }

    // 数据流动
}