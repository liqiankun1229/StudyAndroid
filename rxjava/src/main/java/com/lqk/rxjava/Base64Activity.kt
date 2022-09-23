package com.lqk.rxjava

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

data class ImageBean(var data: String) : Serializable


class Base64Activity : AppCompatActivity() {

    companion object {
        private var KEY_BITMAP = "KEY_BITMAP"

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(VERSION_CODES.O)
        fun show(context: Context) {
            val base64Intent = Intent(context, Base64Activity::class.java)
            val s = context.getDrawable(R.mipmap.img_test)
            s?.let {
                context.startActivity(Intent())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base64)
    }
}