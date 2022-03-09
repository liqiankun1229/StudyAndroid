package com.lqk.butter.ui.activity

import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_zoom.*

/**
 * 拉伸的头部动画
 *
 */
@Route(path = "/butter/zoom")
class ZoomActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_zoom
    }

    override fun initView() {
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
                arrayOf("111111111111111",
                        "222222222222222",
                        "333333333333333",
                        "444444444444444",
                        "555555555555555",
                        "666666666666666"))
        lv_zoom.adapter = arrayAdapter
        val headerView = View.inflate(this, R.layout.layout_header_view, null)
        val imageView = headerView.findViewById<ImageView>(R.id.iv_bg)
        lv_zoom.setmImageView(imageView)
        lv_zoom.addHeaderView(headerView)
        tv_zoom.setOnClickListener {
            startActivity(Intent(this, CustomActivity::class.java))
        }
    }
}
