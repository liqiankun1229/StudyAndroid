package com.lqk.network.thread

import java.util.*

/**
 * 任务
 */
interface Task : Runnable, Result {
    fun setPool(pool: Queue<Task>)
}