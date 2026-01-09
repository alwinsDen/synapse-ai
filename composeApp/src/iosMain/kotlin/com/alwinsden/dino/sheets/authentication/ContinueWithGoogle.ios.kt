package com.alwinsden.dino.sheets.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import authManager.GoogleAuthenticator
import authManager.IOSAuthentication
import com.alwinsden.dino.utilities.UI.DialogLoader
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.android_light_sq_ctn
import dino.composeapp.generated.resources.appleid_sign_in_button
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun TriggerAutoSignIn() {
    TODO("Not yet implemented")
}

actual suspend fun manualTriggerSignIn() {
    TODO("Not yet implemented")
}

@Composable
actual fun ClickableContinueWithGoogle(nonce: String) {
    val scope = rememberCoroutineScope()
    var loaderState by remember { mutableStateOf((false)) }
    val authenticatorClass = remember { GoogleAuthenticator() }
    LaunchedEffect(Unit) {
        loaderState = true
        val readExistingTokenId = authenticatorClass.checkExisting()
        println(readExistingTokenId)
        loaderState = false
    }
    Image(
        painter = painterResource(
            resource = Res.drawable.android_light_sq_ctn
        ),
        contentDescription = "Continue with Google",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth(.5f)
            .wrapContentHeight()
            .clickable {
                scope.launch {
                    val googleIdToken = authenticatorClass.login({ loadingState ->
                        loaderState = loadingState
                    })
                }
            }
    )
    DialogLoader(loaderState)
}

@Composable
actual fun ClickableContinueWithApple(nonce: String) {
    var authenticationClass = remember { IOSAuthentication() }
    var scope = rememberCoroutineScope()
    Image(
        painter = painterResource(resource = Res.drawable.appleid_sign_in_button),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth(.5f)
            .clickable {
                println("Initiate iOS login.")
                scope.launch {
                    authenticationClass.triggerLoginAtRequest()
                }
            }
    )
}