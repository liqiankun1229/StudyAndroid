package com.lqk.butter.ui.activity

/**
 * @author LQK
 * @time 2019/5/4 9:35
 * @remark
 */
interface ViewBinder<T> {
    fun bind(target: T)
}