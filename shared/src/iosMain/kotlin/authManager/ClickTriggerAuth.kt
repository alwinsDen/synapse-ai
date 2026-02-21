package authManager

import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIApplication
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleAuthenticator {
    @OptIn(ExperimentalForeignApi::class)
    suspend fun iosLogin(controlLoadingState: (Boolean) -> Unit, nonce: String) =
        suspendCoroutine<String?> { continuation ->
            val rootUiView = UIApplication.sharedApplication
                .keyWindow?.rootViewController
            if (rootUiView == null) {
                continuation.resume(null)
            } else {
                controlLoadingState(true)
                GIDSignIn.sharedInstance.signInWithPresentingViewController(
                    rootUiView,
                    hint = null,
                    additionalScopes = null,
                    nonce = nonce,
                ) { gidSignInResult, nsError ->
                    if (nsError != null) {
                        controlLoadingState(false)
                        continuation.resume(null)
                    } else {
                        val idToken = gidSignInResult?.user?.idToken
                        val profile = gidSignInResult?.user?.profile
                        if (idToken != null) {
                            continuation.resume(idToken.tokenString)
                        } else {
                            continuation.resume(null)
                        }
                        controlLoadingState(false)
                    }
                }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    suspend fun iosCheckExisting() = suspendCoroutine<String?> { continuation ->
        GIDSignIn.sharedInstance.restorePreviousSignInWithCompletion { user, nsError ->
            if (nsError != null || user == null) {
                continuation.resume(null)
            } else {
                val userIdToken = user?.idToken?.tokenString
                if (userIdToken !== null) {
                    continuation.resume(userIdToken)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    suspend fun iosGoogleLogout() = suspendCancellableCoroutine<Unit> { continuation ->
        GIDSignIn.sharedInstance.signOut()
    }
}