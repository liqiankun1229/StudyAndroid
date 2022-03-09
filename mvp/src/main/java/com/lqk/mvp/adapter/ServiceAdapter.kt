package com.lqk.mvp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.mvp.R
import com.lqk.mvp.bean.ServiceBean

/**
 * @author lqk
 * @date 2018/8/6
 * @time 16:08
 * @remarks
 */
class ServiceAdapter(data: MutableList<ServiceBean>, layoutId: Int = R.layout.item_home_micro_service) :
        BaseQuickAdapter<ServiceBean, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: ServiceBean) {
        if (helper != null && item != null) {
            helper.setText(R.id.tv_service_name, item.name)
            helper.setImageResource(R.id.iv_service_icon, item.img)
        }
    }
}