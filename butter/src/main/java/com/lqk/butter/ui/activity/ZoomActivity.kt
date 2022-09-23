package com.lqk.butter.ui.activity

import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.butter.R
import com.lqk.butter.base.BaseVBActivity
import com.lqk.butter.databinding.ActivityZoomBinding

/**
 * 拉伸的头部动画
 *
 */
@Route(path = "/butter/zoom")
class ZoomActivity : BaseVBActivity<ActivityZoomBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_zoom
    }

    override fun initViewBinding(): ActivityZoomBinding {
        return ActivityZoomBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val arrayAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_single_choice,
            arrayOf(
                "111111111111111",
                "222222222222222",
                "333333333333333",
                "444444444444444",
                "555555555555555",
                "666666666666666"
            )
        )
        vb.lvZoom.adapter = arrayAdapter
        val headerView = View.inflate(this, R.layout.layout_header_view, null)
        val imageView = headerView.findViewById<ImageView>(R.id.iv_bg)
        vb.lvZoom.setmImageView(imageView)
        vb.lvZoom.addHeaderView(headerView)
        vb.lvZoom.setOnClickListener {
            startActivity(Intent(this, CustomActivity::class.java))
        }
    }
}
