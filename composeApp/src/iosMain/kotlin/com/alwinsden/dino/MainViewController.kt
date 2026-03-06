package com.alwinsden.dino

import androidx.compose.ui.window.ComposeUIViewController
import com.alwinsden.dino.authentication.components.GoogleAuthenticatorIos

internal var googleAuthenticatorIos: GoogleAuthenticatorIos? = null

fun MainViewController(googleAuthenticator: GoogleAuthenticatorIos) = ComposeUIViewController {
    googleAuthenticatorIos = googleAuthenticator
    App()
}

