package com.lqk.mvp.ui.activity

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseVBActivity
import com.lqk.mvp.databinding.ActivityLoginBinding
import com.orhanobut.logger.Logger

@Route(path = "/mvp/login")
class LoginActivity : BaseVBActivity<ActivityLoginBinding>() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initVB(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        vb.btnQq.setOnClickListener {
            Logger.d("QQ分享")
            startActivity(Intent(this, QQActivity::class.java))
        }
        vb.btnDd.setOnClickListener {
            startActivity(Intent(this, DDActivity::class.java))
        }
        vb.btnWechat.setOnClickListener {
            startActivity(Intent(this, WeChatActivity::class.java))
        }
        vb.btnWb.setOnClickListener {
            startActivity(Intent(this, WBActivity::class.java))
        }

    }

    private fun getLocation() {
        startActivity(Intent(this, WeChatActivity::class.java))
    }
}
