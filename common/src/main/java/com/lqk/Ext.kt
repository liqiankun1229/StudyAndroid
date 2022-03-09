package com.lqk

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author LQK
 * @time 2019/5/15 10:23
 * @remark 原生控件的扩展函数
 */

/**
 * Glide 给ImageView 加载网络图片
 */
fun ImageView.loadView(url: String) {
    Glide.with(this).load(url).into(this)
}

/**
 * 设置 View 的点击事件监听
 */
fun View.onDoubleClick(listener: DoubleClick) {
    this.setOnClickListener(listener)
}

interface OnDoubleClick : View.OnClickListener {
    var mLastClickTime: Long

//    override fun onClick(v: View?) {
//        v ?: return
//        // 需要防止多次点击 传入的时间间隔 > 0
//        if (doubleTime > 0) {
//            // 获取当前系统时间
//            val currentTimeMills = System.currentTimeMillis()
//            // 计算点击间隔
//            val debounceBetweenTime = currentTimeMills - mLastClickTime
//            if (debounceBetweenTime > doubleTime) {
//                // 更新点击时间
//                mLastClickTime = currentTimeMills
//                func()
//            }
//        } else {
//            func()
//        }
//    }

    fun onDoubleClick(doubleTime: Long = 1000, func: () -> Unit) {
        // 需要防止多次点击 传入的时间间隔 > 0
        if (doubleTime > 0) {
            // 获取当前系统时间
            val currentTimeMills = System.currentTimeMillis()
            // 计算点击间隔
            val debounceBetweenTime = currentTimeMills - mLastClickTime
            if (debounceBetweenTime > doubleTime) {
                // 更新点击时间
                mLastClickTime = currentTimeMills
                func()
            }
        } else {
            func()
        }
    }

}

/**
 * 防止多次点击 控件
 * 默认 一秒内 防多次点击
 */
class DoubleClick(private var doubleTime: Long = 1000, private var func: () -> Unit) : View.OnClickListener {

    private var mLastClickTime: Long

    init {
        this.mLastClickTime = System.currentTimeMillis()
    }

    override fun onClick(v: View?) {
        v ?: return
        // 需要防止多次点击 传入的时间间隔 > 0
        if (doubleTime > 0) {
            // 获取当前系统时间
            val currentTimeMills = System.currentTimeMillis()
            // 计算点击间隔
            val debounceBetweenTime = currentTimeMills - mLastClickTime
            if (debounceBetweenTime > doubleTime) {
                // 更新点击时间
                mLastClickTime = currentTimeMills
                func()
            }
        } else {
            func()
        }
    }
}