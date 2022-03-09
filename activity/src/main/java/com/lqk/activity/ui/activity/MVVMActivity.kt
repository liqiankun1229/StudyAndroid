package com.lqk.activity.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lqk.activity.R
import com.lqk.activity.bean.Person
import com.lqk.activity.databinding.ActivityMvvmBinding

/**
 * dataBinding
 */
class MVVMActivity : AppCompatActivity() {

    var person = Person("LQK", 21, "Boy")
    private lateinit var binding: ActivityMvvmBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_mvvm)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm)
        binding.person = person
        binding.listener = Listener()

        val viewBinding = ActivityMvvmBinding.inflate(layoutInflater)
        viewBinding.tvViewBinding.text = person.name
//        binding.setVariable(Person("LQK", 21, "Boy"))
//        binding.tvFirst.text = "LQK"
//        binding.tvLast.text = "1229"
    }

    inner class Listener {
        fun onClicked(person: Person) {
            Log.d("MVVM", "${person.name}:${person.sex}")
        }

        fun onNameTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            this@MVVMActivity.person.name = charSequence.toString()
            binding.person = person
        }

        fun onSexTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            this@MVVMActivity.person.sex = charSequence.toString()
            binding.person = person
        }
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.anim_activity_open, R.anim.anim_activity_close)
    }
}
