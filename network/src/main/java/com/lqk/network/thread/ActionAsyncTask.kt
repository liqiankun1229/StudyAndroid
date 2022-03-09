package com.lqk.network.thread

import java.util.*

/**
 * 异步执行
 */
class ActionAsyncTask : Action, Task {

    private var mAction: Action
    private var mDone = false
    private var mPool: Queue<Task>? = null

    constructor(action: Action) : this(action, false)

    constructor(action: Action, isDone: Boolean) {
        mAction = action
        mDone = isDone
    }

    override fun run() {
        if (!mDone) {
            synchronized(this) {
                if (!mDone) {
                    call()
                    mDone = true
                }
            }
        }
    }

    override fun call() {
        // Cleanup reference the pool
        mPool = null
        // Doing
        mAction.call()
    }

    override fun isDone(): Boolean {
        return mDone
    }


    override fun setPool(pool: Queue<Task>) {
        mPool = pool
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