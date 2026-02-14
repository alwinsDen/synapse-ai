package com.alwinsden.dino.authentication.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import authManager.GoogleAuthenticator

/**
 * iOS implementation of GoogleAuthProvider using the GoogleSignIn SDK.
 */
class IOSGoogleAuthProvider(
    private val authenticator: GoogleAuthenticator
) : GoogleAuthProvider {

    override suspend fun signIn(nonce: String): Result<String> {
        return try {
            var capturedResult: Result<String> = Result.failure(Exception("Sign-in not completed"))

            val token = authenticator.login { loadingState ->
                // Loading state callback - handled by UI layer
            }

            if (token != null) {
                Result.success(token)
            } else {
                Result.failure(Exception("Sign-in cancelled or failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkExistingCredentials(nonce: String): String? {
        return authenticator.checkExisting()
    }
}

@Composable
actual fun rememberGoogleAuthProvider(): GoogleAuthProvider {
    return remember {
        IOSGoogleAuthProvider(GoogleAuthenticator())
    }
}
