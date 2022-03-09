package com.lqk.mvi

import androidx.lifecycle.ViewModel
import com.lqk.base.BaseActivity

/**
 * @author LQK
 * @time 2022/2/26 10:18
 * @remark MVVM 为基础 版本 Activity 会持有 ViewModel
 *
 */
abstract class BaseMVIActivity<Model : BaseMVIContacts.IModel, VM : ViewModel>
    : BaseActivity(), BaseMVIContacts.IView {

    // 持有 VM 对象
    protected lateinit var mViewModel: VM
    // model 数据的获取 接收用户意图
    protected lateinit var mModel: Model

    // 子类初始化 VM
    abstract fun initViewModel(): VM
    override fun initView() {
        super.initView()
        // mViewModel 赋值
        mViewModel = initViewModel()

    }

    override fun send(intent: BaseMVIContacts.IIntent) {
        mModel.send(intent)
    }

    override fun dispatchState(state: BaseMVIContacts.IState) {

    }
}