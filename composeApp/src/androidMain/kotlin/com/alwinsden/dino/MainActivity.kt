package com.alwinsden.dino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.rive.RiveLog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        RiveLog.logger = RiveLog.LogcatLogger()
        setContent {
            App()
        }
    }
}

@Composable
fun AppAndroidPreview() {
    App()
}