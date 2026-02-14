package com.alwinsden.dino.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.alwinsden.dino.authentication.components.rememberAppleAuthProvider
import com.alwinsden.dino.authentication.components.rememberGoogleAuthProvider
import com.alwinsden.dino.utilities.UI.Defaults
import com.alwinsden.dino.utilities.UI.DialogLoader
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.btn_android_id_rec
import dino.composeapp.generated.resources.btn_apple_id_rec
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/**
 * Composable that displays a clickable Google sign-in button.
 * Uses platform-specific GoogleAuthProvider to handle authentication.
 *
 * @param nonce The nonce value from the server for token validation
 * @param handleReceivedGoogleTokenId Callback invoked when a Google ID token is received
 */
@Composable
fun ClickableContinueWithGoogle(nonce: String, handleReceivedGoogleTokenId: (String) -> Unit) {
    val authProvider = rememberGoogleAuthProvider()
    val scope = rememberCoroutineScope()
    var loaderState by remember { mutableStateOf(false) }

    // Auto sign-in check on component mount
    LaunchedEffect(nonce) {
        if (nonce == Defaults.default) return@LaunchedEffect
        loaderState = true
        authProvider.checkExistingCredentials(nonce)?.let { token ->
            handleReceivedGoogleTokenId(token)
        }
        loaderState = false
    }

    Image(
        painter = painterResource(Res.drawable.btn_android_id_rec),
        contentDescription = "Continue with Google",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth(.5f)
            .clickable {
                scope.launch {
                    if (nonce == Defaults.default) return@launch
                    loaderState = true
                    authProvider.signIn(nonce)
                        .onSuccess { token ->
                            handleReceivedGoogleTokenId(token)
                        }
                        .onFailure { exception ->
                            // TODO: Handle error properly
                            println("Sign-in failed: ${exception.message}")
                        }
                    loaderState = false
                }
            }
    )
    DialogLoader(loaderState)
}

@Composable
fun ClickableContinueWithApple(nonce: String) {
    val scope = rememberCoroutineScope()
    val appleAuthProvider = rememberAppleAuthProvider()
    if (!appleAuthProvider.showAppleAuth()) {
        return
    }
    Image(
        painter = painterResource(resource = Res.drawable.btn_apple_id_rec),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth(.5f)
            .clickable {
                scope.launch {
                    appleAuthProvider.signIn(nonce)
                }
            }
    )
}
