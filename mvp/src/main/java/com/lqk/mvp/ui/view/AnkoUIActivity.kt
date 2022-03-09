package com.lqk.mvp.ui.view

import android.graphics.Color
import com.lqk.mvp.R
import com.lqk.mvp.ui.activity.AnkoActivity
import org.jetbrains.anko.*

/**
 * @author LQK
 * @time 2019/3/7 9:14
 * @remark
 */
class UIAnkoActivity : AnkoComponent<AnkoActivity> {
    override fun createView(ui: AnkoContext<AnkoActivity>) = ui.apply {
        verticalLayout {
            backgroundColor = Color.parseColor("#FF00FF")
            padding = dip(30)
            editText {
                hint = "用户名"
                textSize = 24f
            }
            button {
                text = "分享"
                backgroundColor = R.color.colorBtnBg
            }
            button {
                text = "打开微信"
            }
            button {
                text = "打开小程序"
            }
        }
    }.view
}