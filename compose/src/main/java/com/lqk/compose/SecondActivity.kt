package com.lqk.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @author LQK
 * @time 2022/8/4 15:24
 *
 */
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecondContentView()
        }
    }
}

@Composable
fun SecondContentView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Second")
    }
}
