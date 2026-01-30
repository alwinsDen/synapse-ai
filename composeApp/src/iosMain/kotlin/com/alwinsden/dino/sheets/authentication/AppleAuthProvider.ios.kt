package com.alwinsden.dino.sheets.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import authManager.IOSAuthentication


class IosAppleAuthProvider() : AppleAuthProvider {
    override fun showAppleAuth(): Boolean {
        //only enable Apple login for iOS
        return true
    }

    override suspend fun signIn(nonce: String): Unit {
        IOSAuthentication().triggerLoginAtRequest()
    }
}


@Composable
actual fun rememberAppleAuthProvider(): AppleAuthProvider {
    return remember {
        IosAppleAuthProvider()
    }
}