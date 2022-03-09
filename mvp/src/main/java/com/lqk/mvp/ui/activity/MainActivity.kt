package com.lqk.mvp.ui.activity

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseActivity
import com.lqk.mvp.bean.User
import com.lqk.mvp.presenter.LoginPresenter
import com.lqk.mvp.ui.ILogin
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

@Route(path = "/mvp/main")
class MainActivity : BaseActivity(), ILogin {

    companion object {
        const val TAG = "Main"
    }

    private lateinit var iLoginPresenter: LoginPresenter

    override fun login(userName: String, password: String) {

    }

    override fun loginResult(result: Boolean) {
        toast("$result")
    }

    override fun loginTo(user: User) {

    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        iLoginPresenter = LoginPresenter(this)
        nav.setOnCheckedChangeListener { radioGroup, index ->
            when (radioGroup!!.checkedRadioButtonId) {
                R.id.rb_home_page -> {
                    vp_container.setCurrentItem(0, false)
                }
                R.id.rb_service_page -> {
                    vp_container.setCurrentItem(1, false)
                }
                R.id.rb_life_page -> {
                    vp_container.setCurrentItem(2, false)
                }
                R.id.rb_my_page -> {
                    vp_container.setCurrentItem(3, false)
                }
            }
        }
    }


    var mDownX: Float = -1f
    var mDownY: Float = -1f

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}
