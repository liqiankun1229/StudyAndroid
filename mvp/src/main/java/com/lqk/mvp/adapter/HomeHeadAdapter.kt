package com.lqk.mvp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.mvp.R
import com.lqk.mvp.bean.HomeHeadItemBean

/**
 * @author lqk
 * @date 2018/8/8
 * @time 17:05
 * @remarks
 */
class HomeHeadAdapter(data: MutableList<HomeHeadItemBean>, layoutId: Int = R.layout.item_home_head)
    : BaseQuickAdapter<HomeHeadItemBean, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeHeadItemBean) {
        if (helper != null && item != null) {

            helper.setImageResource(R.id.iv_home_head_icon, item.img)
            helper.setText(R.id.tv_home_head_name, item.name)
        }
    }
}