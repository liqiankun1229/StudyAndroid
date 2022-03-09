package com.lqk.mvp.ui.vm

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

/**
 * @author LQK
 * @time 2022/2/9 13:20
 * @remark
 */
class TestViewModel : ViewModel() {
    var userInfo = MutableLiveData<String>()

    fun doLogin() {

    }

}


class IBaseCustomView<T>(context: Context?) : View(context) {
    init{

    }
    var t: T? = null
}

class BaseViewHolder : RecyclerView.ViewHolder {
    private var mItemView: IBaseCustomView<*>? = null

    constructor(itemView: IBaseCustomView<*>) : super(itemView) {
        this.mItemView = itemView
    }
}

fun text(context: Context) {
    var baseViewHolder = BaseViewHolder(IBaseCustomView<Any>(context))
}