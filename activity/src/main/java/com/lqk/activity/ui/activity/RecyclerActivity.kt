package com.lqk.activity.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lqk.activity.R
import com.lqk.activity.ui.adapter.ServiceAdapter
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        rcv_service.layoutManager = LinearLayoutManager(this)
        rcv_service.adapter = ServiceAdapter(this, mutableListOf<String>())
    }
}
