package authManager

import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleAuthenticator {
    @OptIn(ExperimentalForeignApi::class)
    suspend fun login(controlLoadingState: (Boolean) -> Unit) = suspendCoroutine<String?> { continuation ->
        val rootUiView = UIApplication.sharedApplication
            .keyWindow?.rootViewController
        if (rootUiView == null) {
            continuation.resume(null)
        } else {
            controlLoadingState(true)
            GIDSignIn.sharedInstance.signInWithPresentingViewController(rootUiView) { gidSignInResult, nsError ->
                if (nsError != null) {
                    controlLoadingState(false)
                    continuation.resume(null)
                } else {
                    val idToken = gidSignInResult?.user?.idToken
                    val profile = gidSignInResult?.user?.profile
                    if (idToken != null) {
                        println(idToken.tokenString)
                    } else {
                        continuation.resume(null)
                    }
                    controlLoadingState(false)
                }
            }
        }
    }
}