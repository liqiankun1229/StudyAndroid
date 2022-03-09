package com.lqk.activity.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.lqk.activity.R
import com.lqk.activity.ui.activity.SecondActivity
import kotlinx.android.synthetic.main.fragment_left.*

/**
 * A simple [Fragment] subclass.
 *
 */
class LeftFragment : Fragment() {

    companion object {

        const val CODE = 0x000002
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_left, container, false)
        root.findViewById<Button>(R.id.btn_send_left).setOnClickListener {
            startActivityForResult(Intent(this.context, SecondActivity::class.java), CODE)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Left", "LeftFragment")
        if (requestCode == CODE) {
            tv_left_text.text = "回调"
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
