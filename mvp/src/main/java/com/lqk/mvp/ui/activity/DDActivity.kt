package com.lqk.mvp.ui.activity

import android.annotation.SuppressLint
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import com.lqk.mvp.ddshare.DDUtils
import kotlinx.android.synthetic.main.activity_dd.*

class DDActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_dd
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        DDUtils.initDD(this)
        tv_config.text =
            "${DDUtils.isDDInstalled()} : ${DDUtils.isDDShare()} : ${DDUtils.isDDShareToDing()}"
        btn_txt.setOnClickListener {
            DDUtils.initDD(this)
                .shareTxt("分享到钉钉")
        }
        btn_txt_ding.setOnClickListener {
            DDUtils.shareTxt("分享到钉钉", true)
        }
    }
}