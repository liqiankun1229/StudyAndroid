package com.lqk.mvp

import androidx.viewbinding.ViewBinding
import com.lqk.viewbinding.BaseVBActivity

/**
 * @author LQK
 * @time 2022/8/2 11:10
 *
 */
abstract class BaseMVPActivity<VB : ViewBinding, P : IBaseContacts.IPresenter>
    : BaseVBActivity<VB>() {
}