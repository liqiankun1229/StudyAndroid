package com.lqk.activity.ui.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.lqk.activity.R
import com.lqk.activity.databinding.FragmentLeftBinding
import com.lqk.activity.ui.activity.SecondActivity
import com.lqk.base.BaseVBFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class LeftFragment : BaseVBFragment<FragmentLeftBinding>() {

    companion object {

        const val CODE = 0x000002
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_left
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentLeftBinding {
        return FragmentLeftBinding.inflate(inflater)
    }

    override fun initVB(): View {
        return vb.root
    }

    override fun initListener() {
        super.initListener()
        vb.root.findViewById<Button>(R.id.btn_send_left).setOnClickListener {
            startActivityForResult(Intent(this.context, SecondActivity::class.java), CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Left", "LeftFragment")
        if (requestCode == CODE) {
            vb.tvLeftText.text = "回调"
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
