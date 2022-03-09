package com.lqk.activity.base

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @date 2018/9/15
 * @time 11:24
 * @remarks
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        viewTime()
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(getLayoutId())
        initActivity()
        initView()
        initData()
    }

    open fun viewTime(){}

    override fun getResources(): Resources {
        return super.getResources()
    }

    /**
     * 初始化 界面配置
     */
    abstract fun initActivity()

    /**
     * 初始化 界面控件
     */
    abstract fun initView()

    /**
     * 初始化 界面数据
     */
    abstract fun initData()

    open fun initWindow() {}

}