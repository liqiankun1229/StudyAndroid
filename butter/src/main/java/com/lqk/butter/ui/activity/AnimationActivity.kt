package com.lqk.butter.ui.activity

import android.animation.ObjectAnimator
import android.view.View
import com.lqk.annotations.MyGroupRoute
import com.lqk.butter.R
import com.lqk.butter.base.BaseActivity
import com.lqk.butter.utils.AnimationPathUtil
import com.lqk.butter.utils.PathEvaluator
import com.lqk.butter.utils.PathPoint
import kotlinx.android.synthetic.main.activity_animation.*

@MyGroupRoute("/group/path", "/group")
class AnimationActivity : BaseActivity() {

    lateinit var mPathUtil: AnimationPathUtil

    override fun layoutId(): Int {
        return R.layout.activity_animation
    }

    override fun initView() {

    }

    fun onFabClicked(view: View) {
        // 开启动画
        mPathUtil = AnimationPathUtil()
        mPathUtil.moveTo(0f, 0f)
        mPathUtil.cubicTo(200f, 200f, 300f, 0f, 400f, 100f)
        mPathUtil.lineTo(0f, 0f)

        val animation = ObjectAnimator.ofObject(
            this,
            "haha",
            PathEvaluator(),
            *mPathUtil.pathPofloats.toArray()
        )
        animation.duration = 5000
        animation.addUpdateListener {
            fab.animate()
                .scaleXBy(100f)
                .scaleYBy(100f).duration = 2000

        }
        animation.start()
    }

    fun setHaha(p: PathPoint) {
        fab.translationX = p.getmX()
        fab.translationY = p.getmY()
    }

}
