package com.lqk.mvp.ui.activity

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = "/mvp/login")
class LoginActivity : BaseActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        btn_qq.setOnClickListener {
            startActivity(Intent(this, QQActivity::class.java))
        }
        btn_dd.setOnClickListener {
            startActivity(Intent(this, DDActivity::class.java))
        }
        btn_wechat.setOnClickListener {
            startActivity(Intent(this, WeChatActivity::class.java))
        }
        btn_wb.setOnClickListener {
            startActivity(Intent(this, WBActivity::class.java))
        }

    }

    private fun getLocation() {
        startActivity(Intent(this, WeChatActivity::class.java))
    }
}
