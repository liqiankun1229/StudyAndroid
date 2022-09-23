package com.lqk.web.ui.activity

import com.lqk.web.R
import com.lqk.web.databinding.ActivityWeb2Binding
import com.unionpay.UPPayAssistEx

class WebActivity : BaseVBActivity<ActivityWeb2Binding>() {

    override fun initLayoutId(): Int {
        return R.layout.activity_web2
    }

    override fun initViewBinding(): ActivityWeb2Binding {
        return ActivityWeb2Binding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.tvPay.setOnClickListener {
            UPPayAssistEx.startPay(this, null, null, "786493409505075790311", "01")
        }
    }
}