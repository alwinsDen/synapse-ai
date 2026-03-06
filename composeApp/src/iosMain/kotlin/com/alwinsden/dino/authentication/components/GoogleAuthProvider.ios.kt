package com.alwinsden.dino.authentication.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.alwinsden.dino.googleAuthenticatorIos
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * iOS implementation of GoogleAuthProvider using the GoogleSignIn SDK via Swift bridge.
 */
class IOSGoogleAuthProvider(private val authenticator: GoogleAuthenticatorIos) : GoogleAuthProvider {

    override suspend fun signIn(nonce: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            authenticator.iosLogin(nonce = nonce) { token ->
                if (token != null) {
                    continuation.resume(Result.success(token))
                } else {
                    continuation.resume(Result.failure(Exception("Sign-in cancelled or failed")))
                }
            }
        }
    }

    override suspend fun checkExistingCredentials(nonce: String): String? {
        return suspendCancellableCoroutine { continuation ->
            authenticator.iosCheckExisting { token ->
                continuation.resume(token)
            }
        }
    }

    override suspend fun logoutFromGoogle() {
        authenticator.iosGoogleLogout()
    }
}

@Composable
actual fun rememberGoogleAuthProvider(): GoogleAuthProvider {
    val authenticator = checkNotNull(googleAuthenticatorIos) {
        "GoogleAuthenticatorIos must be set via MainViewController before use"
    }
    return remember { IOSGoogleAuthProvider(authenticator) }
}

interface GoogleAuthenticatorIos {
    fun iosLogin(nonce: String, completion: (String?) -> Unit)
    fun iosCheckExisting(completion: (String?) -> Unit)
    fun iosGoogleLogout()
}