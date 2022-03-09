package com.lqk.mvp.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author lqk
 * @date 2018/8/7
 * @time 10:17
 * @remarks
 */
class HomeItemBean : MultiItemEntity {

    override val itemType: Int
        get() = type

    // 需要加载的布局识标
    var type: Int = 0
    // 2*2 的功能表 type : 1
    var functionList: MutableList<FunctionGategoryBean>? = null

    // 通知 type ：2

    // 预约 type ：3

    // 5*2 服务列表 type ：4
    var serviceList: MutableList<ServiceBean>? = null

    // 办事 type ：5

    // 新闻 type ：6

}