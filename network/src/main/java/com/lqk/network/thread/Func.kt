package com.lqk.network.thread

/**
 * @author LQK
 * @time 2018/10/15 16:27
 * @remark
 */
interface Func<Out> {

    fun call(): Out
}