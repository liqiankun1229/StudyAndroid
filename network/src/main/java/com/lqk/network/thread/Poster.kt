package com.lqk.network.thread

interface Poster{
    companion object {
        var ASYNC = 0x10101010
        var SYNC = 0x20202020
    }

    /**
     * 异步执行
     */
    fun async(runnable: Task)

    /**
     * 同步执行
     */
    fun sync(runnable: Task)


    /**
     * 销毁
     */
    fun dispose()
}