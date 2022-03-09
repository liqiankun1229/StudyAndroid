package com.lqk.media

import android.os.Bundle
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.lqk.R
import kotlinx.android.synthetic.main.activity_adbrowser.*

class ADBrowserActivity : AppCompatActivity() {

    companion object {
        const val KEY_URL = "KEY_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adbrowser)
        web.webChromeClient = WebChromeClient()
        web.loadUrl(intent.getStringExtra(KEY_URL) ?: "")
    }
}
