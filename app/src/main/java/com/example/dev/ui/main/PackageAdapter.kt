package com.example.dev.ui.main

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.dev.R
import com.example.dev.bean.PackageBean

/**
 * @author LQK
 * @time 2022/8/1 11:13
 *
 */
class PackageAdapter(dataList: MutableList<PackageBean>, layoutId: Int = R.layout.item_package) :
    BaseQuickAdapter<PackageBean, BaseViewHolder>(layoutId, dataList) {

    override fun convert(holder: BaseViewHolder, item: PackageBean) {
        holder.setText(R.id.tv_package, item.name)
    }
}