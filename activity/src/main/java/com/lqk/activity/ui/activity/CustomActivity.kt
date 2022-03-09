package com.lqk.activity.ui.activity

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.LayoutInflaterCompat
import com.lqk.activity.R
import com.lqk.activity.base.BaseActivity

class CustomActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_custom
    }

    override fun viewTime() {
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                val time = System.currentTimeMillis()
                val view = delegate.createView(parent, name, context, attrs)
                Log.d(
                    "TAG",
                    "onCreateView: ${System.currentTimeMillis() - time}"
                )
                return view
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        })
    }

    override fun initActivity() {
//        AsyncLayoutInflater(this).inflate(
//            R.layout.activity_custom,
//            null,
//            object : AsyncLayoutInflater.OnInflateFinishedListener {
//                override fun onInflateFinished(view: View, resid: Int, parent: ViewGroup?) {
//
//                }
//            })
    }


    override fun initView() {
        AlertDialog.Builder(this)
            .create().show()
    }

    override fun initData() {

    }
}
