package com.lqk.mvp.ui.activity

import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseVBActivity
import com.lqk.mvp.databinding.ActivityServiceBinding
import com.lqk.mvp.widget.CustomXpopup

@Route(path = "/mvp/ServiceActivity")
class ServiceActivity : BaseVBActivity<ActivityServiceBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_service
    }

    override fun initVB(): ActivityServiceBinding {
        return ActivityServiceBinding.inflate(layoutInflater)
    }

    override fun initView() {

        vb.sv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            run {
                // oldScrollY 滑动前的 Y 轴坐标
                // scrollY 滑动后的 Y 轴坐标
                val top = (scrollY / 4 - vb.flTop.height)
                if (top <= 0) {
                    val alpha = (vb.flTop.height + top) / vb.flTop.height.toDouble() * 255
                    val layoutParams: FrameLayout.LayoutParams =
                        vb.flTop.layoutParams as FrameLayout.LayoutParams
                    layoutParams.setMargins(0, top, 0, 0)
                    vb.flTop.background.alpha = alpha.toInt()
                    vb.flTop.layoutParams = layoutParams

                    val bottomTop = scrollY / 2
                    val layoutParams1: FrameLayout.LayoutParams =
                        vb.llBottom.layoutParams as FrameLayout.LayoutParams
                    layoutParams1.setMargins(0, -bottomTop, 0, 0)
                    vb.llBottom.layoutParams = layoutParams1
                } else {
                    val layoutParams: FrameLayout.LayoutParams =
                        vb.flTop.layoutParams as FrameLayout.LayoutParams
                    layoutParams.setMargins(0, 0, 0, 0)
                    vb.flTop.background.alpha = 255
                    vb.flTop.layoutParams = layoutParams
                }
            }
        }
        vb.flTop.setOnClickListener {
            Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            Log.d("Click", "TOP")
        }
        vb.llCenter.setOnClickListener {
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
            Log.d("Click", "点击")
        }
        vb.view.setOnClickListener {
            Toast.makeText(this, "View", Toast.LENGTH_SHORT).show()
            Log.d("Click", "View")
            CustomXpopup(this).show()
        }
    }

}
