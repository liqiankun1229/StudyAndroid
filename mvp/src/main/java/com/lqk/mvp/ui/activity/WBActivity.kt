package com.lqk.mvp.ui.activity

import android.widget.Toast
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseVBActivity
import com.lqk.mvp.databinding.ActivityWeiboBinding
import com.lqk.mvp.share.WeiboShareUtils

/**
 * @author LQK
 * @date 2021/10/27
 * @remark
 */
class WBActivity : BaseVBActivity<ActivityWeiboBinding>() {



    override fun layoutId(): Int {
        return R.layout.activity_weibo
    }

    override fun initVB(): ActivityWeiboBinding {
        return ActivityWeiboBinding.inflate(layoutInflater)
    }

    override fun initView() {
        WeiboShareUtils.initWeibo(this)

    }

    fun shareTxt(view: android.view.View) {
        if (!WeiboShareUtils.instance.shareTxt(
                this, "分享"
            )
        ) {
            Toast.makeText(this, "没有安装微博", Toast.LENGTH_SHORT).show()
        }
    }
}