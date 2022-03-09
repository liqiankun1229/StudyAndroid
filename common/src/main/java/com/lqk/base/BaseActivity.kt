package com.lqk.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @date 2018/9/3
 * @time 10:11
 * @remarks 封装base Activity
 * 功能: 初始化一下
 */
abstract class BaseActivity : AppCompatActivity() {

    // xml 的获取
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    open fun initActivity() {
        initWindow()
        setContentView(getLayoutId())
        initView()
        initListener()
        initEvent()
        initData()
    }

    // 沉浸式
    open fun initWindow() {}

    // 初始化 View 相关信息
    open fun initView() {}

    // 监听事件
    open fun initListener() {}

    open fun initEvent() {}

    // 数据请求
    open fun initData() {}
}