package com.lqk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @author LQK
 * @date 2021/9/20
 * @remark
 */
abstract class BaseVBFragment<T : ViewBinding> : BaseFragment() {

    protected lateinit var vb: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = initRootView(inflater, container, savedInstanceState)
        return root
    }


    /**
     * MVP 架构不需要复写
     * MVVM 需要复写 初始化
     */
    open fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = initViewBinding(inflater)
        initVB()
        return vb.root
    }

    abstract fun initViewBinding(inflater: LayoutInflater): T

    abstract fun initVB(): View
}