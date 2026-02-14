package com.alwinsden.dino.authentication.components

import androidx.compose.runtime.Composable

interface AppleAuthProvider {
    fun showAppleAuth(): Boolean
    suspend fun signIn(nonce: String): Unit
}

@Composable
expect fun rememberAppleAuthProvider(): AppleAuthProvider