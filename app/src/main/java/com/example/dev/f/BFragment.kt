package com.example.dev.f

import android.view.LayoutInflater
import android.view.View
import com.example.dev.R
import com.example.dev.databinding.FragmentABinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author LQK
 * @date 2021/10/9
 * @remark
 */
class BFragment : BaseFragment() {

    override fun initLayout(): Int {
        return R.layout.fragment_a
    }

    private lateinit var vb: FragmentABinding

    override fun initVB(inflater: LayoutInflater): View {
        vb = FragmentABinding.inflate(inflater)
        return vb.root
    }

    override fun initListener() {
        super.initListener()
        vb.icBack.setOnClickListener {
            LiveEventBus.get<String>(LoginActivity.EVENT_BACK).post("")
        }
        vb.tvContent.setOnClickListener {
            LiveEventBus.get<String>(LoginActivity.EVENT_PAGE).post("B")
        }
    }

    override fun initView() {
        super.initView()
        vb.tvContent.text = "B"
    }


}