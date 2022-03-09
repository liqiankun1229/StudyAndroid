package com.lqk.activity.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.lqk.activity.R
import com.lqk.activity.ext.FragmentListener
import com.lqk.activity.helper.BottomNavigationViewHelper
import com.lqk.activity.ui.fragment.Blank1Fragment
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity(), FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val bFragment = Blank1Fragment()
        val bundle = Bundle()
        bundle.putString("name", "Hello LQK")
        bFragment.arguments = bundle
        replaceFragment(bFragment)

        BottomNavigationViewHelper.disableShiftMode(btn_nav_home)
        btn_nav_home.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    val fragment = supportFragmentManager.fragments[0] as Blank1Fragment
                    fragment.setText("Home")
                }
                R.id.nav_service -> {
                    val fragment = supportFragmentManager.fragments[0] as Blank1Fragment
                    fragment.setText("Service")
                }
                R.id.nav_life -> {
                    val fragment = supportFragmentManager.fragments[0] as Blank1Fragment
                    if (fragment.listener == null) {
                        fragment.listener = this
                    } else {
                        Toast.makeText(this, "${fragment.tag} 支持回调", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.nav_mine -> {
                    // 获取到 fragment 中的 name 数据
                    val fragment = supportFragmentManager.fragments[0] as Blank1Fragment
                    Toast.makeText(this, fragment.getText(), Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        btn_nav_home.menu.getItem(2).isChecked = true
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    override fun getData(): Any {
        return "喵了个咪"
    }

    fun getStringData(): String {
        return "玩了个嗨"
    }
}
