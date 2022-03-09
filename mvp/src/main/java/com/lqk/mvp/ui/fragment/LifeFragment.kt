package com.lqk.mvp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lqk.mvp.R
import com.lqk.mvp.adapter.StringAdapter
import kotlinx.android.synthetic.main.fragment_life.*

class LifeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_life, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_top.setOnClickListener {
            fl.visibility = View.GONE
            rcv.scrollToPosition(0)
        }
        rcv.layoutManager = LinearLayoutManager(this.context)
        rcv.adapter = StringAdapter(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"))
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        fl.visibility = View.VISIBLE
                    } else {
                        fl.visibility = View.GONE
                    }
                }

                Log.d("dy", "$dy")
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }
}
