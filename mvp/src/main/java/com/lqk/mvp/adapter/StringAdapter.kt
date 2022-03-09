package com.lqk.mvp.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.mvp.R

/**
 * @author lqk
 * @date 2018/8/6
 * @time 16:08
 * @remarks
 */
class StringAdapter(data: MutableList<String>, layoutId: Int = R.layout.item_string) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        if (helper != null && item != null) {
            helper.itemView.findViewById<TextView>(R.id.item_string_name).text = item
        }
    }
}