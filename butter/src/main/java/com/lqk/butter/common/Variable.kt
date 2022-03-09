package com.lqk.butter.common

import android.content.ComponentName

/**
 * @author LQK
 * @time 2019/2/27 16:46
 * @remark 存储全局变量
 */
object Variable {

    // 现在使用的启动图标
    var oldIcon: ComponentName? = null
    // 需要更改的启动图标
    var newIcon: ComponentName? = null
}