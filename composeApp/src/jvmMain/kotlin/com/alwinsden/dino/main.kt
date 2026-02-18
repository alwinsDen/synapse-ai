package com.alwinsden.dino

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "demo",
    ) {
        Test()
    }
}

@Composable
fun Test() {
    Text("Hi aliwnsss")
}