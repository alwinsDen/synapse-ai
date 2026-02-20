package com.alwinsden.dino.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alwinsden.dino.botChatInterface.BotChatInterface
import com.alwinsden.dino.botInterface.BotInterface
import com.alwinsden.dino.settingsInterface.SettingsInterface
import com.alwinsden.dino.startup.UserStartupPage

object NavigationController {

    @Composable
    fun NavRoutes() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = LoginWindow) {
            composable<LoginWindow> {
                UserStartupPage()
            }
            composable<BotWindow> {
                BotInterface()
            }
            composable<BotChatWindow> {
                BotChatInterface()
            }
            composable<SettingsNavigation> {
                SettingsInterface()
            }
        }
    }
}