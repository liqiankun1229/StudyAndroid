package com.lqk.mvp.bean

import androidx.annotation.DrawableRes

/**
 * @author lqk
 * @date 2018/8/8
 * @time 17:07
 * @remarks
 */
data class HomeHeadItemBean(val name: String,
                            val icon: String,
                            @DrawableRes val img: Int)