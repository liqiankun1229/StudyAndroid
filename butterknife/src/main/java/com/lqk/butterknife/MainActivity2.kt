package com.lqk.butterknife

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/butterknife/main")
class MainActivity2 : AppCompatActivity() {

    lateinit var btnMain:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnMain = findViewById(R.id.btn2)
    }
}