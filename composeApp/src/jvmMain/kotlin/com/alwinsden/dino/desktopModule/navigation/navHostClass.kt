package com.alwinsden.dino.desktopModule.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alwinsden.dino.desktopModule.startup.Startup
import kotlinx.serialization.Serializable

object NavigationControllerSynapseDesktop {

    @Composable
    fun NavRoutesDesktop() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = StartupNav) {
            composable<StartupNav> {
                Startup()
            }
        }
    }
}

@Serializable
object StartupNav {
}