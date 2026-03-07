package com.alwinsden.dino.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alwinsden.dino.authentication.components.rememberAppleAuthProvider
import com.alwinsden.dino.authentication.components.rememberGoogleAuthProvider
import com.alwinsden.dino.requestManager.ApiState
import com.alwinsden.dino.requestManager.StartUpLaunchViewModel
import com.alwinsden.dino.utilities.UI.DialogLoader
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.android_light_sq_na_4x
import dino.composeapp.generated.resources.appleid_button_4x
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
fun ClickableContinueWithGoogle(handleReceivedGoogleTokenId: (String, String) -> Unit) {
    val authProvider = rememberGoogleAuthProvider()
    var loaderState by remember { mutableStateOf(false) }
    val vm = viewModel { StartUpLaunchViewModel() }
    val vmManualLogin = viewModel { StartUpLaunchViewModel() }
    val nonceState = vm.nonce.collectAsState()
    val nonceStateManualLogin = vmManualLogin.nonceManual.collectAsState()

    // Auto sign-in check on component mount
    LaunchedEffect(nonceState.value) {
        when (val state = nonceState.value) {
            is ApiState.Idle -> {}
            is ApiState.Loading -> {
                loaderState = true
            }

            is ApiState.Success<String> -> {
                authProvider.checkExistingCredentials(state.response)?.let { token ->
                    handleReceivedGoogleTokenId(token, state.response)
                }
                loaderState = false
            }

            is ApiState.Error -> {
                println("ERROR.logs ${state.message}")
                loaderState = false
            }
        }
    }

    // Manual sign-in check on component mount (on mount it remains idle)
    LaunchedEffect(nonceStateManualLogin.value) {
        when (val state = nonceStateManualLogin.value) {
            is ApiState.Idle -> {}
            is ApiState.Loading -> {
                loaderState = true
            }

            is ApiState.Success<String> -> {
                authProvider.signIn(state.response)
                    .onSuccess { token ->
                        handleReceivedGoogleTokenId(token, state.response)
                    }
                    .onFailure { exception ->
                        println("Sign-in failed: ${exception.message}")
                    }
                loaderState = false
            }

            is ApiState.Error -> {
                println("ERROR: ${state.message}")
                loaderState = false
            }
        }
    }

    Image(
        painter = painterResource(Res.drawable.android_light_sq_na_4x),
        contentDescription = "Continue with Google",
        modifier = Modifier.height(48.dp).width(48.dp)
            .clickable {
                vmManualLogin.initiateManual()
            }
    )
    DialogLoader(loaderState)
}

@Composable
fun ClickableContinueWithApple() {
    val scope = rememberCoroutineScope()
    val appleAuthProvider = rememberAppleAuthProvider()
    if (!appleAuthProvider.showAppleAuth()) {
        return
    }
    Image(
        painter = painterResource(resource = Res.drawable.appleid_button_4x),
        contentDescription = "",
        modifier = Modifier
            .height(48.dp).width(48.dp)
            .clickable {
                scope.launch {
                    /*
                    * TODO: Here the nonce is hard-coded since we haven;t been able to test
                    *  apple login yet. If implementing follow same as Google View Model part.
                    * */
                    appleAuthProvider.signIn("nonce")
                }
            }
    )
}
