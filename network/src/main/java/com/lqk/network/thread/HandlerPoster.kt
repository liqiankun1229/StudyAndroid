package com.lqk.network.thread

import android.os.Handler
import android.os.SystemClock
import android.os.Looper
import android.os.Message
import com.lqk.network.thread.Poster.Companion.ASYNC
import com.lqk.network.thread.Poster.Companion.SYNC
import java.util.*

class HandlerPoster : Handler, Poster {

    companion object {
        // 最大保存任务数量
        var MAX_MILLIS_INSIDE_HANDLE_MESSAGE = 16
    }

    private var mAsyncDispatcher: Dispatcher
    private var mSyncDispatcher: Dispatcher

    constructor(looper: Looper, maxMillisInsideHandleMessage: Int, onlyAsync: Boolean) : super(looper) {

        MAX_MILLIS_INSIDE_HANDLE_MESSAGE = maxMillisInsideHandleMessage

        mAsyncDispatcher = Dispatcher(LinkedList<Task>(),
                object : Dispatcher.IPoster {
                    override fun sendMessage() {
                        this@HandlerPoster.sendMessage(ASYNC)
                    }
                })
        mSyncDispatcher = if (onlyAsync) {
            mAsyncDispatcher
        } else {
            Dispatcher(LinkedList<Task>(),
                    object : Dispatcher.IPoster {
                        override fun sendMessage() {
                            this@HandlerPoster.sendMessage(SYNC)
                        }
                    })
        }
    }

    override fun dispose() {
        this.removeCallbacksAndMessages(null)
        this.mAsyncDispatcher.dispose()
        this.mSyncDispatcher.dispose()
    }

    override fun async(runnable: Task) {
        mAsyncDispatcher.offer(runnable)
    }

    override fun sync(runnable: Task) {
        mSyncDispatcher.offer(runnable)
    }

    override fun handleMessage(msg: Message) {
        when {
            msg.what == ASYNC -> mAsyncDispatcher.dispatch()
            msg.what == SYNC -> mSyncDispatcher.dispatch()
            else -> super.handleMessage(msg)
        }
    }

    private fun sendMessage(what: Int) {
        if (!sendMessage(obtainMessage(what))) {
            throw RuntimeException("Could not send handler message")
        }
    }

    private class Dispatcher internal constructor(private val mPool: Queue<Task>, private var mPoster: IPoster?) {
        private var isActive: Boolean = false

        internal fun offer(task: Task) {
            synchronized(mPool) {
                // offer to queue pool
                mPool.offer(task)
                // set the task pool reference
                task.setPool(mPool)
                if (!isActive) {
                    isActive = true
                    // send again message
                    val poster = mPoster
                    poster?.sendMessage()
                }
            }
        }

        internal fun dispatch() {
            var rescheduled = false
            try {
                val started = SystemClock.uptimeMillis()
                while (true) {
                    var runnable = poll()
                    if (runnable == null) {
                        synchronized(mPool) {
                            // Check again, this time in synchronized
                            runnable = poll()
                            if (runnable == null) {
                                isActive = false
                                return
                            }
                        }
                    }
                    runnable!!.run()
                    val timeInMethod = SystemClock.uptimeMillis() - started
                    if (timeInMethod >= MAX_MILLIS_INSIDE_HANDLE_MESSAGE) {
                        // send again message
                        val poster = mPoster
                        poster?.sendMessage()
                        // rescheduled is true
                        rescheduled = true
                        return
                    }
                }
            } finally {
                isActive = rescheduled
            }
        }

        internal fun dispose() {
            mPool.clear()
            mPoster = null
        }

        private fun poll(): Runnable? {
            synchronized(mPool) {
                return try {
                    mPool.poll()
                } catch (e: NoSuchElementException) {
                    e.printStackTrace()
                    null
                }

            }
        }

        internal interface IPoster {
            fun sendMessage()
        }
    }
}