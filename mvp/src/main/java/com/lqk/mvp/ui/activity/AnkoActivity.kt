package com.lqk.mvp.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lqk.mvp.R
import org.jetbrains.anko.*

class AnkoActivity : AppCompatActivity() {

//    override fun layoutId(): Int {
//        return R.layout.activity_anko
//    }

//    override fun initView() {
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_anko)

//        initView()
        UIAnkoActivity().setContentView(this)
    }
}

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
