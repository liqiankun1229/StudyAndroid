package com.lqk.activity.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lqk.activity.R
import com.lqk.activity.ui.activity.SecondActivity


/**
 * A simple [Fragment] subclass.
 *
 */
class ParentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager
                .beginTransaction()
                .add(R.id.fragment_left, LeftFragment())
                .add(R.id.fragment_right, RightFragment())
                .commit()
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == SecondActivity.CODE) {
            childFragmentManager.fragments[0].onActivityResult(requestCode, resultCode, data)
            childFragmentManager.fragments[1].onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
