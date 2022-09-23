package com.lqk.butter.ui.activity

import android.content.Intent
import com.lqk.annotations.MyGroupRoute
import com.lqk.butter.R
import com.lqk.butter.base.BaseVBActivity
import com.lqk.butter.databinding.ActivityCustomBinding

/**
 * 屏幕适配
 * -- CustomLayout 容器
 */
@MyGroupRoute("/group/custom")
class CustomActivity : BaseVBActivity<ActivityCustomBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_custom
    }

    override fun initViewBinding(): ActivityCustomBinding {
        return ActivityCustomBinding.inflate(layoutInflater)
    }

    override fun initView() {
        vb.tvTop.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
