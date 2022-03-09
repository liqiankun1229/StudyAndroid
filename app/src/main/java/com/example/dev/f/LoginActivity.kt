package com.example.dev.f

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.dev.R
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val EVENT_BACK = "EVENT_BACK"
        const val EVENT_PAGE = "EVENT_PAGE"

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private var cur: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initEvent()
        initListener()
        initData()
    }

    private var fragmentStack = Stack<String>()

    private fun loadF(f: BaseFragment, mode: String) {
        supportFragmentManager.commit {
            replace(R.id.root, f)
            setReorderingAllowed(true)
            addToBackStack(mode)
            fragmentStack.push(mode)
        }
    }

    private fun initView() {
        loadF(AFragment(), "A")
    }

    private fun initEvent() {
        LiveEventBus.get<String>(EVENT_BACK).observeForever {
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
        LiveEventBus.get<String>(EVENT_PAGE).observeForever {
            when (it) {
                "A" -> {
                    loadF(BFragment(), "A")
                }
                "B" -> {
                    loadF(CFragment(), "B")
                }
                "C" -> {
                    loadF(DFragment(), "C")
                }
                "D" -> {
                    loadF(AFragment(), "D")
                }
                else -> {
                    loadF(AFragment(), "A")
                }
            }
        }
    }

    private fun initListener() {
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            run {
                cur = fragment as BaseFragment
            }
        }
    }

    private fun initData() {}

    override fun onBackPressed() {
        if (fragmentStack.size <= 1) {
            this.finish()
        } else {
            fragmentStack.pop()
            super.onBackPressed()
        }
    }
}