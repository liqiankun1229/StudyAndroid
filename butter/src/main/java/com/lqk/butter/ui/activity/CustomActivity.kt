package com.lqk.butter.ui.activity

import android.content.Intent
import com.lqk.annotations.MyGroupRoute
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom.*

/**
 * 屏幕适配
 * -- CustomLayout 容器
 */
@MyGroupRoute("/group/custom")
class CustomActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_custom
    }

    override fun initView() {
        tv_top.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
