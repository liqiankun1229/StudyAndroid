package com.lqk.butter.common

import android.view.View

/**
 * @author LQK
 * @time 2019/2/27 21:55
 * @remark
 */
object Ext {
    /**
     * 检查需要修改的icon是否存在
     */
    fun inIconMap(string: String): Boolean {
        for (entry in Constant.mapIcon) {
            if (entry.value == string) {
                return true
            }
        }
        return false
    }

}

/**
 * View 点击
 */
fun View.onClick(action: () -> Unit) {
    this.setOnClickListener {
        action()
    }
}