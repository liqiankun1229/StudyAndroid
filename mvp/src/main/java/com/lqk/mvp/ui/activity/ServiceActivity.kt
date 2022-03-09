package com.lqk.mvp.ui.activity

import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import com.lqk.mvp.widget.CustomXpopup
import kotlinx.android.synthetic.main.activity_service.*

@Route(path = "/mvp/ServiceActivity")
class ServiceActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_service
    }

    override fun initView() {

        sv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            run {
                // oldScrollY 滑动前的 Y 轴坐标
                // scrollY 滑动后的 Y 轴坐标
                val top = (scrollY / 4 - fl_top.height)
                if (top <= 0) {
                    val alpha = (fl_top.height + top) / fl_top.height.toDouble() * 255
                    val layoutParams: FrameLayout.LayoutParams = fl_top.layoutParams as FrameLayout.LayoutParams
                    layoutParams.setMargins(0, top, 0, 0)
                    fl_top.background.alpha = alpha.toInt()
                    fl_top.layoutParams = layoutParams

                    val bottomTop = scrollY / 2
                    val layoutParams1: FrameLayout.LayoutParams = ll_bottom.layoutParams as FrameLayout.LayoutParams
                    layoutParams1.setMargins(0, -bottomTop, 0, 0)
                    ll_bottom.layoutParams = layoutParams1
                } else {
                    val layoutParams: FrameLayout.LayoutParams = fl_top.layoutParams as FrameLayout.LayoutParams
                    layoutParams.setMargins(0, 0, 0, 0)
                    fl_top.background.alpha = 255
                    fl_top.layoutParams = layoutParams
                }
            }
        }
        fl_top.setOnClickListener {
            Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            Log.d("Click", "TOP")
        }
        ll_center.setOnClickListener {
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
            Log.d("Click", "点击")
        }
        view.setOnClickListener {
            Toast.makeText(this, "View", Toast.LENGTH_SHORT).show()
            Log.d("Click", "View")
            CustomXpopup(this).show()
        }
    }

}
