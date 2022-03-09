package com.example.dev.f

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author LQK
 * @date 2021/10/9
 * @remark
 */
abstract class BaseFragment : Fragment() {

    abstract fun initLayout(): Int

    lateinit var root: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initVB(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        initListener()
        initData()
    }

    open fun initView() {}
    open fun initEvent() {}
    open fun initListener() {}
    open fun initData() {}

    abstract fun initVB(inflater: LayoutInflater): View
}