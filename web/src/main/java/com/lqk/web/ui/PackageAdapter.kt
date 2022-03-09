package com.lqk.web.ui

import android.widget.Button
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lqk.web.R
import com.lqk.web.ui.bean.VersionInfo

/**
 * @author LQK
 * @time 2022/2/24 10:12
 * @remark
 */
class PackageAdapter(
    list: MutableList<VersionInfo>,
    layoutId: Int = R.layout.item_package
) : BaseQuickAdapter<VersionInfo, BaseViewHolder>(layoutId, list) {
    override fun convert(holder: BaseViewHolder, item: VersionInfo) {
        holder.setText(R.id.tv_name, "${typeDesc(item)}: ${item.appId}-${item.versionName}")
        // 按钮信息
        holder.getView<Button>(R.id.action).setOnClickListener {
            listener?.openPackage(item)
        }
    }

    private fun typeDesc(versionInfo: VersionInfo): String {
        return when (versionInfo.packageType) {
            -1 -> {
                "预置离线包"
            }
            0 -> {
                "线上离线包"
            }
            else -> {
                "线上包"
            }
        }
    }

    private var listener: Listener? = null
    fun initListener(l: Listener) {
        this.listener = l
    }

    interface Listener {
        fun openPackage(versionInfo: VersionInfo)
    }
}