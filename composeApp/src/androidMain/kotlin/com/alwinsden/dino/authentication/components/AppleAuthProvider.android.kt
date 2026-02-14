package com.alwinsden.dino.authentication.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


class AndroidAppleAuthProvider() : AppleAuthProvider {
    override fun showAppleAuth(): Boolean {
        //only enable Apple login for iOS
        return false
    }

    override suspend fun signIn(nonce: String): Unit {

    }
}

@Composable
actual fun rememberAppleAuthProvider(): AppleAuthProvider {
    return remember {
        AndroidAppleAuthProvider()
    }
}