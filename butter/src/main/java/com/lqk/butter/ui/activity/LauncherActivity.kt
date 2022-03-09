package com.lqk.butter.ui.activity

import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import com.lqk.butter.common.onClick
import com.lqk.butter.zxing.CaptureActivity
import kotlinx.android.synthetic.main.activity_launcher.*

/**
 * 启动页
 */
class LauncherActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_launcher
    }

    override fun initView() {
//        if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish()
//            return
//        }

        tv_butter_show.setOnClickListener {
            tv_butter_show.text = "12345"
            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
        }
        tv_icon_show.setOnClickListener {

            // startActivity(Intent(this, ChangeIconActivity::class.java)
            // .putExtra(ChangeIconActivity.KEY_ICON_NAME, "${et_icon_name.text}"))

            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
            finish()
        }
        tv_icon_anim.onClick {
            // 开始一个动画
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_do)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    startActivity(Intent(this@LauncherActivity, AnimationActivity::class.java))
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
            tv_icon_anim.startAnimation(anim)
        }
        tv_zoom.setOnClickListener {
            // startActivity(Intent(this, ZoomActivity::class.java))
            // startActivity(Intent(this, CustomActivity::class.java))
//            startActivity(Intent(this, SlidingCardActivity::class.java))
            startActivity(Intent(this, CaptureActivity::class.java))
        }
        tv_damping.setOnClickListener {
            //            var userList = linkedMapOf<String, String>()
//            val type = object : TypeToken<LinkedHashMap<String, String>>() {}.type
//            val data = ""
//
//            userList = Gson().fromJson(data, type) ?: linkedMapOf()
//            startActivity(Intent(this, SearchBookContentsActivity::class.java)
//                    .setAction(Intents.SearchBookContents.ACTION))
            startActivity(Intent(this, ZxingActivity::class.java))
        }
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//
//                startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
//                finish()
//                this.cancel()
//            }
//        }, 2000)
    }

    override fun onBackPressed() {
        // 启动页不支持返回操作
    }
}
