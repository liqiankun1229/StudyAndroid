package com.lqk.mvvm

import androidx.viewbinding.ViewBinding
import com.lqk.base.BaseVBActivity

/**
 * @author LQK
 * @time 2022/2/26 20:35
 * @remark
 */
abstract class BaseMVVMActivity<VB : ViewBinding, VM : BaseMVVMViewModel<*>> :
    BaseVBActivity<VB>() {

    protected lateinit var vm: VM

    override fun initVM() {
        super.initVM()
        vm = initViewModel()
        // 数据绑定
        viewBinding
        vm.data.observe(this) {
            // 数据更新
        }
        vm.loadData()

    }

    abstract fun initViewModel(): VM
}