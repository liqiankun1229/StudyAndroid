package com.lqk.mvp.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.mvp.R
import com.lqk.mvp.bean.HomeItemBean
import com.tencent.mm.opensdk.utils.Log

/**
 * @author lqk
 * @date 2018/8/7
 * @time 10:18
 * @remarks
 */
class HomeAdapter(data: MutableList<HomeItemBean>) : BaseMultiItemQuickAdapter<HomeItemBean, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.item_home_function)
        addItemType(2, R.layout.item_home_notice)
        addItemType(3, R.layout.item_home_appointment)
        addItemType(4, R.layout.item_home_service)
        addItemType(5, R.layout.item_home_progress)
        addItemType(6, R.layout.item_home_news)
    }

    override fun convert(helper: BaseViewHolder, item: HomeItemBean) {
        if (item != null) {
            when (item.type) {
                1 -> {
                    helper!!.itemView.findViewById<RecyclerView>(R.id.rcv_function).layoutManager =
                            GridLayoutManager(helper.itemView!!.context, 2)
                    val functionCategoryAdapter = FunctionCategoryAdapter(item.functionList!!)
                    functionCategoryAdapter.setOnItemClickListener { adapter, view, position ->
                        run {
                            if (item.functionList!![position].name == "我要预约") {
                                Log.d("TT", item.functionList!![position].name)
                            } else {
                                ARouter.getInstance()
                                        .build("/mvp/transact")
                                        .withString("title", item.functionList!![position].name)
                                        .navigation()
                            }
                        }
                    }
                    helper.itemView.findViewById<RecyclerView>(R.id.rcv_function).adapter = functionCategoryAdapter
                }
                2 -> {

                }
                3 -> {

                }
                4 -> {
                    helper!!.itemView.findViewById<RecyclerView>(R.id.rcv_hot_service)
                            .layoutManager = GridLayoutManager(helper.itemView!!.context, 5)
                    val serviceAdapter = ServiceAdapter(item.serviceList!!)
                    serviceAdapter.setOnItemClickListener { adapter, view, position ->
                        run {
                            Log.d("TT", item.serviceList!![position].name)
                        }
                    }
                    helper.itemView.findViewById<RecyclerView>(R.id.rcv_hot_service)
                            .adapter = serviceAdapter

                }
                5 -> {

                }
                6 -> {
                }

            }
        }
    }


}