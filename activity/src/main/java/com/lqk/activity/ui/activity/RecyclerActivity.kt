package com.lqk.activity.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.lqk.activity.R
import com.lqk.activity.databinding.ActivityRecyclerBinding
import com.lqk.activity.ui.adapter.ServiceAdapter
import com.lqk.base.BaseVBActivity

class RecyclerActivity : BaseVBActivity<ActivityRecyclerBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_recycler
    }

    override fun initViewBinding(): ActivityRecyclerBinding {
        return ActivityRecyclerBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        viewBinding.rcvService.layoutManager = LinearLayoutManager(this)
        viewBinding.rcvService.adapter = ServiceAdapter(this, mutableListOf<String>())
    }

}
