package com.lqk.mvvm

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lqk.viewbinding.BaseVBActivity

/**
 * @author LQK
 * @time 2022/8/1 15:49
 * VB : ViewBinding xml 视图生成, 替代 findViewById<View>(ViewId)
 * VM : ViewModel 用户操作指令处理(请求网络, 数据保存等耗时操作)
 */
abstract class BaseMVVMActivity<VB : ViewBinding, VM : ViewModel>
    : BaseVBActivity<VB>() {

    private val vm: VM by lazy {
        initVM()
    }

    abstract fun initVM(): VM
}