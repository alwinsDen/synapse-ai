package com.alwinsden.dino.authentication.components

import androidx.compose.runtime.Composable

/**
 * Interface for Google authentication functionality across platforms.
 * Provides a common abstraction for Google Sign-In operations.
 */
interface GoogleAuthProvider {
    /**
     * Initiates the sign-in flow with the provided nonce.
     *
     * @param nonce The nonce value from the server for token validation
     * @return Result containing the Google ID token on success, or an error
     */
    suspend fun signIn(nonce: String): Result<String>

    /**
     * Checks for existing credentials without prompting the user.
     * Useful for automatic sign-in on app launch.
     *
     * @return The existing Google ID token if available, null otherwise
     */
    suspend fun checkExistingCredentials(nonce: String): String?
    suspend fun logoutFromGoogle(): Unit
}

/**
 * Platform-specific factory function to create a GoogleAuthProvider instance.
 * Each platform provides its own implementation.
 */
@Composable
expect fun rememberGoogleAuthProvider(): GoogleAuthProvider
