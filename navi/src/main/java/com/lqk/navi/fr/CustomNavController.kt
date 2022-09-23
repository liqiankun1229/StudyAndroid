package com.lqk.navi.fr

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavHost

/**
 * @author LQK
 * @time 2022/8/30 15:59
 *
 */
class CustomNavController(private val mContext: Context) : NavHost {
    override val navController: NavController
        get() = NavController(context = mContext)
}