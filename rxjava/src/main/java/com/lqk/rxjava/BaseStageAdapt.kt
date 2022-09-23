package com.lqk.rxjava

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author LQK
 * @time 2022/5/4 13:12
 *
 */
fun getResIdFromType(type: Int): Int {
    return R.layout.item_stage1
}

class BaseStageAdapt<T : BaseStageBean>(var dataList: MutableList<T>) :
    BaseMultiItemQuickAdapter<BaseStageBean, BaseViewHolder>() {

    init {
        // 添加所有的 View <-> Data 绑定
        dataList.forEach {
            // 动态添加

            addItemType(it.itemType, getResIdFromType(it.itemType))

        }
    }

    override fun convert(holder: BaseViewHolder, item: BaseStageBean) {
        item.loadView(holder)
    }


}