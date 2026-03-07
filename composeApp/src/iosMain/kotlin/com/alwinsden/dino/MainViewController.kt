package com.alwinsden.dino

import androidx.compose.ui.window.ComposeUIViewController
import com.alwinsden.dino.swiftBridge.GoogleAuthenticatorIos
import com.alwinsden.dino.swiftBridge.NativeViewFactory

internal var googleAuthenticatorIos: GoogleAuthenticatorIos? = null
internal var riveViewControllerIos : NativeViewFactory? = null

fun MainViewController(googleAuthenticator: GoogleAuthenticatorIos, riveViewController : NativeViewFactory) = ComposeUIViewController {
    googleAuthenticatorIos = googleAuthenticator
    riveViewControllerIos = riveViewController
    App()
}

