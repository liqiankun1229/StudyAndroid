package com.lqk.network.thread

import java.util.*

/**
 * 同步执行
 */
class ActionSyncTask : Action, Task {

    private val lock = java.lang.Object()

    private val mAction: Action
    private var mDone = false
    private var mPool: Queue<Task>? = null

    constructor(action: Action) {
        this.mAction = action
    }

    override fun call() {
        // Cleanup reference the pool
        mPool = null
        // Doing
        mAction.call()
    }

    override fun run() {
        if (!mDone) {
            synchronized(lock) {
                if (!mDone) {
                    call()
                    mDone = true
                    try {
                        lock.notifyAll()
                    } catch (ignored: Exception) {
                    }

                }
            }
        }
    }

    fun waitRun() {
        if (!mDone) {
            synchronized(lock) {
                while (!mDone) {
                    try {
                        lock.wait()
                    } catch (ignored: InterruptedException) {
                    }

                }
            }
        }
    }

    fun waitRun(waitMillis: Long, waitNanos: Int, cancelOnTimeOut: Boolean) {
        if (!mDone) {
            synchronized(lock) {
                if (!mDone) {
                    try {
                        lock.wait(waitMillis, waitNanos)
                    } catch (ignored: InterruptedException) {
                    } finally {
                        if (!mDone && cancelOnTimeOut)
                            mDone = true
                    }
                }
            }
        }
    }

    override fun setPool(pool: Queue<Task>) {
        mPool = pool
    }

    override fun isDone(): Boolean {
        return mDone
    }

    override fun cancel() {
        if (!mDone) {
            synchronized(this) {
                mDone = true

                if (mPool != null) {

                    @Synchronized
                    if (mPool != null) {
                        try {
                            mPool!!.remove(this)
                        } catch (e: Exception) {
                            e.stackTrace
                        } finally {
                            mPool = null
                        }
                    }
                }
            }
        }
    }

}