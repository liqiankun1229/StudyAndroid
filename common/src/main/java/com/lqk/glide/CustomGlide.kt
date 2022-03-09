package com.lqk.glide

import android.app.Activity

/**
 * @author LQK
 * @time 2019/6/26 20:27
 * @remark
 */
class CustomGlide {

    var instance: CustomGlide? = null

    init {
        if (instance == null){
            synchronized(lock = this){
                if (instance == null){
                    instance = CustomGlide()
                }
            }
        }
    }

    fun with(activity: Activity): CustomGlide {
        return this
    }
}