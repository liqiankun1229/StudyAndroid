package com.lqk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author LQK
 * @time 2019/7/26 9:45
 * @remark
 */
abstract class BaseFragment : Fragment() {

    var root: View? = null

    abstract fun initLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(initLayoutId(), container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    // 初始化 View 相关信息
    open fun initView() {}

    // 监听事件
    open fun initListener() {}

    // 数据请求
    open fun initData() {}
}