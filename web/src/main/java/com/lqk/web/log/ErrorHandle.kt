package com.lqk.web.log

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.lqk.web.application.WebApplication
import java.lang.Thread.UncaughtExceptionHandler

/**
 * @author LQK
 * @time 2022/6/18 10:50
 *
 * 全局异常捕获
 *
 */
class ErrorHandle private constructor() : UncaughtExceptionHandler {

    companion object {
        const val TAG = "ErrorHandle"
        val lock = Any()

        @SuppressLint("StaticFieldLeak")
        private var mErrorHandle: ErrorHandle? = null

        /**
         * 初始化
         */
        fun getInstance(): ErrorHandle {
            synchronized(lock) {
                if (mErrorHandle == null) {
                    synchronized(lock) {
                        if (mErrorHandle == null) {
                            mErrorHandle = ErrorHandle()
                        }
                    }
                }
                return mErrorHandle!!
            }
        }
    }

    // 全局上下文
    private var mContext: Context? = null
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 主线程
     * 发生闪退后的处理
     */
    val errorMessage = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // 正确提示方式
            if (msg.obj != null) {
                if (msg.obj is Throwable) {
                    (WebApplication.getApplication() as WebApplication).topActivity?.let {
                        Toast.makeText(
                            it,
                            (msg.obj as Throwable).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                (WebApplication.getApplication() as WebApplication).topActivity?.let {
                    Toast.makeText(
                        it,
                        "程序发生异常, 准备重启",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            super.handleMessage(msg)
        }
    }

    // 捕获 Looper.loop() 消息轮询期间产生的异常
    private val handlerRunnable = Runnable {
        while (true) {
            try {
                // 对 Looper.loop 进行捕获异常 减小崩溃
                Looper.loop()
            } catch (e: Throwable) {
                e.printStackTrace()
                this.uncaughtException(Thread.currentThread(), e)
            }
        }
    }

    /**
     * 发送一个事件 在主线程启动个 Runnable 用于捕获 Looper.loop() 异常
     */
    private fun handlerTry() {
        Handler(Looper.getMainLooper()).post(handlerRunnable)
    }

    /**
     * 初始化
     */
    fun initError(context: Context) {
        this.mContext = context
        handlerTry()
        // 系统默认的 Un)caughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该 CrashHandler 为程序默认的处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        Log.d(TAG, "initError: 初始化")
    }

    /**
     * 处理程序启动后 未捕获的异常
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // 异常
        Thread {
//            Looper.prepare()
            // 打印错误堆栈信息
            Log.d(TAG, "uncaughtException: ${thread.name}")
            Log.d(TAG, "uncaughtException: ${throwable.javaClass.name}")
            Log.d(TAG, "uncaughtException: ${throwable.message}")
            for (t in throwable.stackTrace) {
                Log.d(TAG, "uncaughtException: $t")
            }
            Log.d(TAG, "uncaughtException: 程序发生异常, 准备重启")
            // 发送提醒
            val msg = Message.obtain()
            msg.what = 0
            msg.obj = throwable
            errorMessage.sendMessage(msg)
            // 保存错误日志
//            Looper.loop()
//            SystemClock.sleep(2000)
//            // 重置一些参数 关闭activity
//            // 退出程序
//            Process.killProcess(Process.myPid())
//            // 关闭应用
//            exitProcess(0)
        }.start()
    }
}