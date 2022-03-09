package com.lqk.mvp.ui.activity

import android.util.Log
import com.google.gson.Gson
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import com.lqk.mvp.share.QQUtils
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 * @author LQK
 * @date 2021/10/21
 * @remark
 */
class QQActivity : BaseActivity() {
    companion object {
        const val TAG = "QQActivity"
    }

    override fun layoutId(): Int {
        return R.layout.activity_anko
    }

    override fun initView() {

    }

    fun shareQQ(view: android.view.View) {
        QQUtils.shareText(this, "测试分享", object : IUiListener {
            override fun onComplete(p0: Any?) {
                Log.d(TAG, "onComplete: ${Gson().toJson(p0)}")
            }

            override fun onError(p0: UiError?) {
                Log.d(TAG, "onError: ${Gson().toJson(p0)}")
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel: ")
            }

            override fun onWarning(p0: Int) {
                Log.d(TAG, "onWarning: ${Gson().toJson(p0)}")
            }
        })
    }

    fun shareQZone(view: android.view.View) {}
}