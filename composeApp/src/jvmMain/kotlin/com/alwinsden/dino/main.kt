package com.alwinsden.dino

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alwinsden.dino.desktopModule.navigation.NavigationControllerSynapseDesktop

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Synapse Desktop",
    ) {
        Runner()
    }
}

@Composable
fun Runner() {
    NavigationControllerSynapseDesktop.NavRoutesDesktop()
}