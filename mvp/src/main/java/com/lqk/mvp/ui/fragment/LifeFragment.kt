package com.lqk.mvp.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lqk.base.BaseVBFragment
import com.lqk.mvp.R
import com.lqk.mvp.adapter.StringAdapter
import com.lqk.mvp.databinding.FragmentLifeBinding

class LifeFragment : BaseVBFragment<FragmentLifeBinding>() {

    override fun initLayoutId(): Int {
        return R.layout.fragment_life
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentLifeBinding {
        return FragmentLifeBinding.inflate(layoutInflater)
    }

    override fun initVB(): View {
        return vb.root
    }

    override fun initListener() {
        super.initListener()
        vb.tvTop.setOnClickListener {
            vb.fl.visibility = View.GONE
            vb.rcv.scrollToPosition(0)
        }
        vb.rcv.layoutManager = LinearLayoutManager(this.context)
        vb.rcv.adapter = StringAdapter(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"))
        vb.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var layoutManager = recyclerView.layoutManager
                if (layoutManager != null) {
                    layoutManager as LinearLayoutManager
                    val first = layoutManager.findFirstVisibleItemPosition()
                    Log.d("first", "$first")
                    val fView = layoutManager.findViewByPosition(first)
                    if (fView != null) {
                        val itemHeight = fView.height
                        if (itemHeight != null) {
                            var scroll = first * itemHeight - fView.top
                        }
                    }
                    if (first > 1) {
                        vb.fl.visibility = View.VISIBLE
                    } else {
                        vb.fl.visibility = View.GONE
                    }
                }

                Log.d("dy", "$dy")
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

}
