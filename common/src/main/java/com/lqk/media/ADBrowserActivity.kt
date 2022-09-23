package com.lqk.media

import android.webkit.WebChromeClient
import com.lqk.R
import com.lqk.base.BaseVBActivity
import com.lqk.databinding.ActivityAdbrowserBinding

class ADBrowserActivity : BaseVBActivity<ActivityAdbrowserBinding>() {

    companion object {
        const val KEY_URL = "KEY_URL"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_adbrowser
    }

    override fun initViewBinding(): ActivityAdbrowserBinding {
        return ActivityAdbrowserBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        viewBinding.web.webChromeClient = WebChromeClient()
        viewBinding.web.loadUrl(intent.getStringExtra(KEY_URL) ?: "")
    }
}
