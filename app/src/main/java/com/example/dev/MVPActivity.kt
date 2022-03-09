package com.example.dev

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * @author LQK
 * @date 2021/9/29
 * @remark
 */
class MVPActivity:AppCompatActivity()  {

    private var mainPresenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBtn(view: View) {
        if (mainPresenter == null)
            mainPresenter = MainPresenter(this)
        mainPresenter?.updateText()
    }

    fun onClickBtnSync(view: View) {
        if (mainPresenter == null)
            mainPresenter = MainPresenter(this)
        mainPresenter?.updateTextSync()
    }

    fun onClickVideo(view: View) {
        startActivity(Intent(this, VideoActivity::class.java))
    }

    fun updateTxt(str: String) {
        findViewById<TextView>(R.id.tv).text = str
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}