package com.lqk.activity.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lqk.activity.R
import com.lqk.activity.ui.activity.SecondActivity
import kotlinx.android.synthetic.main.fragment_right.*

/**
 * A simple [Fragment] subclass.
 *
 */
class RightFragment : Fragment() {

    companion object {
        const val CODE = 0x000002
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        val root = inflater.inflate(R.layout.fragment_right, container, false)
//        root.findViewById<Button>(R.id.btn_send_right).setOnClickListener {
//            startActivityForResult(Intent(this.context, SecondActivity::class.java), CODE)
//        }
        return inflater.inflate(R.layout.fragment_right, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_send_right.setOnClickListener {
            startActivityForResult(Intent(this.context, SecondActivity::class.java), CODE)

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Right", "RightFragment")
        if (requestCode == CODE) {
            tv_right_text.text = data?.getStringExtra("dataResult")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}
