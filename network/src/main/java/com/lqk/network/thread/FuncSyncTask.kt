package com.lqk.network.thread

import java.util.*

/**
 *
 */
@SuppressWarnings("unused")
class FuncSyncTask<T> : Func<T>, Task {

    private val lock = java.lang.Object()

    private val mFunc: Func<T>
    private var mResult: T? = null
    private var mDone = false
    private var mPool: Queue<Task>? = null

    constructor(func: Func<T>) {
        this.mFunc = func
    }

    override fun call(): T {
        // 清除
        mPool = null
        // Doing
        return mFunc.call()
    }

    override fun run() {
        if (!mDone) {
            synchronized(lock) {
                if (!mDone) {
                    mResult = call()
                    mDone = true
                    try {
                        lock.notifyAll()
                    } catch (ignored: Exception) {
                    }
                }
            }
        }
    }

    fun waitRun(): T? {
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
        return mResult
    }

    fun waitRun(waitMillis: Long, waitNanos: Int, cancelOnTimeOut: Boolean): T? {
        if (!mDone) {
            synchronized(lock) {
                if (!mDone) {
                    try {
                        lock.wait(waitMillis, waitNanos)
                    } catch (ignored: InterruptedException) {
                    } finally {
                        if (!mDone && cancelOnTimeOut) {
                            mDone = true
                        }
                    }
                }
            }
        }
        return mResult
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