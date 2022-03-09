package com.lqk.mvp.base.activity

import android.os.Bundle

/**
 * @author lqk
 * @date 2018/8/7
 * @time 15:10
 * @remarks
 */
abstract class BaseMVPActivity : BaseActivity() {

    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
    }
}