package com.lqk.mvp.widget

import android.content.Context
import android.widget.Toast
import com.lqk.mvp.R
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.dialog_layout.view.*

/**
 * @author LQK
 * @time 2019/10/23 23:15
 * @remark
 */
class CustomXpopup : AttachPopupView {
    constructor(context: Context) : super(context)

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_layout
    }

    override fun onCreate() {
        super.onCreate()
        tv1.setOnClickListener {
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
        }
        tv1.setOnClickListener {
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
        }
    }
}