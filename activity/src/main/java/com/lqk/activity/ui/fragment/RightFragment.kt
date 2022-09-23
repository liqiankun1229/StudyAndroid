package com.lqk.activity.ui.fragment


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.lqk.activity.R
import com.lqk.activity.databinding.FragmentRightBinding
import com.lqk.activity.ui.activity.SecondActivity
import com.lqk.base.BaseVBFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class RightFragment : BaseVBFragment<FragmentRightBinding>() {

    companion object {
        const val CODE = 0x000002
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_right
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentRightBinding {
        return FragmentRightBinding.inflate(inflater)
    }

    override fun initVB(): View {
        return vb.root
    }

    override fun initListener() {
        super.initListener()
        vb.btnSendRight.setOnClickListener {
            startActivityForResult(Intent(this.context, SecondActivity::class.java), CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Right", "RightFragment")
        if (requestCode == CODE) {
            vb.tvRightText.text = data?.getStringExtra("dataResult")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}
