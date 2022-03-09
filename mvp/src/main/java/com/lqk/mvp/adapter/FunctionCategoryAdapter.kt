package com.lqk.mvp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.mvp.R
import com.lqk.mvp.bean.FunctionGategoryBean

/**
 * @author lqk
 * @date 2018/8/7
 * @time 10:10
 * @remarks
 */
class FunctionCategoryAdapter(data: MutableList<FunctionGategoryBean>, layoutId: Int = R.layout.item_home_func_category)
    : BaseQuickAdapter<FunctionGategoryBean, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: FunctionGategoryBean) {
        if (helper != null && item != null) {
            helper.setText(R.id.tv_title, item.name)
            helper.setText(R.id.tv_des, item.des)
            helper.setImageResource(R.id.iv_img, item.img)
        }
    }
}