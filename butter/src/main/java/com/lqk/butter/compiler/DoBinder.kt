package com.lqk.butter.compiler

import android.app.Activity
import com.lqk.butter.ui.activity.ViewBinder

/**
 * @author LQK
 * @time 2019/5/4 21:33
 * @remark
 */
object DoBinder {
    fun bind(activity: Activity) {
        val name = "${activity.javaClass.name}_ViewBinder"
        try {
            val viewBinder = Class.forName(name).newInstance() as ViewBinder<Activity>
            viewBinder.bind(activity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}