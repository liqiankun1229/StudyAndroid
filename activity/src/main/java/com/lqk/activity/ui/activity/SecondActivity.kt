package com.lqk.activity.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lqk.activity.R
import okhttp3.*
import java.io.IOException

class SecondActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "生命周期 SecondActivity "
        const val CODE = 0x000004
    }

    private var btDialog: Button? = null
    private var ret: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.d(TAG, "onCreate: ")

        val mainActivity = intent
        // 接收 Activity 传值 getXXXExtra() 根据 key 值 从 Bundle 中取出对应类型的数据
        val strStartUp = mainActivity.getStringExtra("startupMode") ?: "未知错误"
        Toast.makeText(this, strStartUp, Toast.LENGTH_SHORT).show()
        init()
    }

    private fun init() {
        btDialog = findViewById(R.id.bt_dialog)
        btDialog!!.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@SecondActivity)
            alertDialog.setMessage("弹出对话框情况下生命周期的变化")
                .setPositiveButton("确定") { dialog, _ -> run { dialog.dismiss() } }
                .setNegativeButton("取消") { dialog, _ ->
                    run {
                        val okHttpClient = OkHttpClient()
                        val request =
                            Request.Builder().url("http://47.95.3.33:8080/iTalker/api/account/test")
                                .build()
                        val call = okHttpClient.newCall(request)
                        call.enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.d(TAG, "onFailure: 请求失败")
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {

                                Log.d(TAG, "onResponse: " + response.body!!.string())
                            }
                        })
                        // String string = response.body().string();
                        // Log.d(TAG, "onClicked: " + string);
                        dialog.dismiss()
                    }
                }.create().show()
        }

        ret = findViewById(R.id.ret)
        ret!!.setOnClickListener {
            setResult(CODE, intent.putExtra("dataResult", "返回数据"))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }
}
