package com.lqk.mvp.widget

import android.content.Context
import android.widget.Toast
import com.lqk.mvp.R
import com.lqk.mvp.databinding.DialogLayoutBinding
import com.lxj.xpopup.core.AttachPopupView
import org.jetbrains.anko.layoutInflater

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

    lateinit var vb: DialogLayoutBinding

    override fun addInnerContent() {
        vb = DialogLayoutBinding.inflate(context.layoutInflater)
        attachPopupContainer.addView(vb.root)
    }

    override fun onCreate() {
        super.onCreate()

        vb.tv1.setOnClickListener {
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
        }
        vb.tv2.setOnClickListener {
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
        }
    }
}