package com.lqk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author LQK
 * @time 2022/8/1 15:33
 *
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivity()
    }

    /**
     * 初始化 Activity (窗口/页面/事件/数据)
     */
    open fun initActivity() {
        initWindow()
        setContentView(layoutId())
        initView()
        initListener()
        initEvent()
        initData()
    }

    open fun initWindow() {}

    abstract fun layoutId(): Int

    open fun initView() {}

    open fun initListener() {}

    open fun initEvent() {}

    open fun initData() {}


}