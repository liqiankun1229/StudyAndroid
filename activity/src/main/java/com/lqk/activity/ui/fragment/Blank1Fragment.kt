package com.lqk.activity.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.lqk.activity.R
import com.lqk.activity.ext.FragmentListener
import com.lqk.activity.ui.activity.Main2Activity


@SuppressLint("ValidFragment")
class Blank1Fragment : Fragment {

    var name: String? = null

    var textView: TextView? = null
    var btnSet: Button? = null
    var btnGet: Button? = null
    var btnActivityGet: Button? = null
    var listener: FragmentListener? = null

    constructor()

    constructor(listener: FragmentListener) : super() {
        this.listener = listener
    }


    /**
     * 当 Fragment 被加载到 activity 中时被回调 的生命周期
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is FragmentListener) {

        }
    }

    /**
     * 释放资源 防止内存泄漏
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blank1, container, false)

        textView = view.findViewById(R.id.tv_name)
        btnSet = view.findViewById(R.id.btn_set)
        btnGet = view.findViewById(R.id.btn_get)
        btnActivityGet = view.findViewById(R.id.btn_activity_get)
        btnSet!!.setOnClickListener {
            name = "被强制修改了"
            Toast.makeText(this.context, "ok", Toast.LENGTH_SHORT).show()
        }
        btnGet!!.setOnClickListener {
            if (listener != null) {
                val nameData = listener!!.getData()
                Toast.makeText(this.context, "$nameData", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "不支持回调 Activity 方法", Toast.LENGTH_SHORT).show()
            }
        }
        btnActivityGet!!.setOnClickListener {
            val activity = this.activity as Main2Activity
            textView!!.text = activity.getStringData()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        if (isAdded) {
            // 判断 Fragment 是否已经 依附在 Activity 上
            if (arguments != null) {
                name = arguments!!.getString("name")
                textView!!.text = name
            }
        }
    }

    fun getText(): String {
        return if (name == null) {
            ""
        } else {
            name!!
        }
    }

    fun setText(str: String) {
        if (textView != null) {
            textView!!.text = str
            name = "显示：$str"
        } else {
            name = "未赋值"
        }
    }
}
