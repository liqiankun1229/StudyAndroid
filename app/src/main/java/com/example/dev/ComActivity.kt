package com.example.dev

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class ComActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeContent(mutableStateOf(true))
        }
    }

    @Composable
    fun ComposeContent(state: MutableState<Boolean>) {
        LaunchedEffect(state.value, block = {

        })
        Column(
            Modifier
                .fillMaxSize(1f)
        ) {
            Text(
                modifier = Modifier
                    .background(color = Color(android.graphics.Color.parseColor("#ececec")))
                    .align(alignment = Alignment.CenterHorizontally)
                    .clickable {
                        var t = this
                        Toast
                            .makeText(this@ComActivity, "文字点击", Toast.LENGTH_SHORT)
                            .show()
                    }, text = "compose"
            )
        }
    }
}