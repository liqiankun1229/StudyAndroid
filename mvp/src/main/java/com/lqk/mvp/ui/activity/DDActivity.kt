package com.lqk.mvp.ui.activity

import android.annotation.SuppressLint
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseVBActivity
import com.lqk.mvp.databinding.ActivityDdBinding
import com.lqk.mvp.ddshare.DDUtils

class DDActivity : BaseVBActivity<ActivityDdBinding>() {


    override fun layoutId(): Int {
        return R.layout.activity_dd
    }

    override fun initVB(): ActivityDdBinding {
        return ActivityDdBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        super.initView()
        DDUtils.initDD(this)
        vb.tvConfig.text =
            "${DDUtils.isDDInstalled()} : ${DDUtils.isDDShare()} : ${DDUtils.isDDShareToDing()}"
        vb.btnTxt.setOnClickListener {
            DDUtils.initDD(this)
                .shareTxt("分享到钉钉")
        }
        vb.btnTxtDing.setOnClickListener {
            DDUtils.shareTxt("分享到钉钉", true)
        }
    }
}