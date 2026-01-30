package com.alwinsden.dino.sheets.authentication

import androidx.compose.runtime.Composable

interface AppleAuthProvider {
    fun showAppleAuth(): Boolean
    suspend fun signIn(nonce: String): Unit
}

@Composable
expect fun rememberAppleAuthProvider(): AppleAuthProvider