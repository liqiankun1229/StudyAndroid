package com.lqk.mvp.bean

import androidx.annotation.DrawableRes

/**
 * @author lqk
 * @date 2018/8/7
 * @time 10:08
 * @remarks
 */
data class FunctionGategoryBean(val name: String,
                                val des: String,
                                val icon: String,
                                @DrawableRes val img: Int)