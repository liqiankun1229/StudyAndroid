package com.lqk.network.thread

/**
 * 执行返回
 */
interface Result {
    fun isDone(): Boolean

    fun cancel()
}