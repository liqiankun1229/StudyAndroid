package com.lqk.mvi

import com.lqk.annotations.MyGroupRoute
import com.lqk.route.Info

/**
 * @author LQK
 * @time 2022/3/6 19:38
 * @remark
 */
@MyGroupRoute("/info/data")
class InfoImpl : IInfo {
    override fun loadInfo(): Info {
        return Info("lqk")
    }
}