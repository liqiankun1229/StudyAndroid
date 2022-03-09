package com.lqk.network.thread

import android.os.Handler
import android.os.Looper

/**
 * 线程切换工具
 */
@SuppressWarnings("WeakerAccess")
object Run {

    private val lock = java.lang.Object()

    private var uiPoster: HandlerPoster? = null
    private var backgroundPoster: HandlerPoster? = null

    fun getUiHandler(): Handler? {
        return getUiPoster()
    }

    private fun getUiPoster(): HandlerPoster? {
        if (uiPoster == null) {
            synchronized(Run::class.java) {
                uiPoster = HandlerPoster(Looper.getMainLooper(), 16, false)
            }
        }
        return uiPoster
    }

    /**
     * 后台执行操作
     */
    fun getBackgroundHandler(): Handler? {
        return getBackgroundPoster()
    }

    private fun getBackgroundPoster(): HandlerPoster? {
        if (backgroundPoster == null) {
            synchronized(Run::class.java) {
                if (backgroundPoster == null) {
                    val thread = object : Thread("ThreadRunHandler") {
                        override fun run() {
                            Looper.prepare()
                            synchronized(Run::class.java) {
                                if (Looper.myLooper() != null) {
                                    backgroundPoster = HandlerPoster(Looper.myLooper()!!, 3 * 1000, true)
                                    try {
                                        lock.notifyAll()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            Looper.loop()
                        }
                    }
                    thread.isDaemon = true
                    thread.priority = Thread.MAX_PRIORITY
                    thread.start()
                    try {
                        lock.wait()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return backgroundPoster
    }

    /**
     * 在子线程
     */
    fun onBackground(action: Action): Result {
        val poster = getBackgroundPoster()
        if (Looper.myLooper() == poster!!.getLooper()) {
            action.call()
            return ActionAsyncTask(action, true)
        }
        val task = ActionAsyncTask(action)
        poster.async(task)
        return task
    }

    /**
     * 在主线程 异步
     */
    fun onUiAsync(action: Action): Result {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.call()
            return ActionAsyncTask(action, true)
        }
        val task = ActionAsyncTask(action)
        getUiPoster()?.async(task)
        return task
    }

    /**
     * 在主线程 同步
     */
    fun onUiSync(action: Action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.call()
            return
        }
        val poster = ActionSyncTask(action)
        getUiPoster()?.sync(poster)
        poster.waitRun()
    }

    fun onUiSync(action: Action, waitMillis: Long, cancelOnTimeOut: Boolean) {
        onUiSync(action, waitMillis, 0, cancelOnTimeOut)
    }

    fun onUiSync(action: Action, waitMillis: Long, waitNanos: Int, cancelOnTimeOut: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.call()
            return
        }
        val poster = ActionSyncTask(action)
        getUiPoster()?.sync(poster)
        poster.waitRun(waitMillis, waitNanos, cancelOnTimeOut)
    }

    fun <T> onUiSync(func: Func<T>): T {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return func.call()
        }
        val poster = FuncSyncTask<T>(func)
        getUiPoster()?.sync(poster)
        return poster.waitRun()!!
    }

    fun <T> onUiSync(func: Func<T>, waitMillis: Long, cancelOnTimeOut: Boolean): T? {
        return onUiSync<T>(func, waitMillis, 0, cancelOnTimeOut)
    }

    fun <T> onUiSync(func: Func<T>, waitMillis: Long, waitNanos: Int, cancelOnTimeOut: Boolean): T? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return func.call()
        }
        val poster = FuncSyncTask<T>(func)
        getUiPoster()?.sync(poster)
        return poster.waitRun(waitMillis, waitNanos, cancelOnTimeOut)
    }

    fun dispose() {
        if (uiPoster != null) {
            uiPoster!!.dispose()
            uiPoster = null
        }
    }
}