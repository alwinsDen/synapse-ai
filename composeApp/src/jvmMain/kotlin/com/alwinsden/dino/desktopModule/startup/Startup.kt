package com.alwinsden.dino.desktopModule.startup

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Startup() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(
                onClick = {
                    //
                }
            ) {
                Text("Select project folder.")
            }
        }
    }
}