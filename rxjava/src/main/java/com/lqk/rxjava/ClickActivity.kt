package com.lqk.rxjava

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_click.*

class ClickActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ClickActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click)
        initListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        iv1.setOnClickListener {
            startActivity(Intent(this, RxJavaActivity::class.java))
        }
//
//        iv2.setOnClickListener {
//            Log.d(TAG, "initListener: IV2")
//        }
//
//        iv2.setOnTouchListener { view, motionEvent ->
//            run {
//                Log.d(TAG, "initListener: ${motionEvent.x} : ${motionEvent.y}")
//                if (motionEvent.x < 100) {
//                    return@run false
//                }
//                return@run true
//            }
//        }
    }
}


class TView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}