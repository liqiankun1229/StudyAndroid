package com.lqk.rxjava

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author LQK
 * @time 2022/5/4 17:58
 *
 */
abstract class BaseStageBean : IBaseStageBean {
    override val itemType: Int
        get() {
            return -1
        }

    override fun loadView(holder: BaseViewHolder) {

    }

    override fun loadListener() {

    }

    override fun loadData() {

    }
}


interface IBaseStageBean : MultiItemEntity{
    fun loadView(holder: BaseViewHolder)
    fun loadListener()
    fun loadData()
}