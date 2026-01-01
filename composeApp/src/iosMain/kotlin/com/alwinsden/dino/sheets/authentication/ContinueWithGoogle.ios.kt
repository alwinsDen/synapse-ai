package com.alwinsden.dino.sheets.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.android_light_sq_ctn
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
    Image(
        painter = painterResource(
            resource = Res.drawable.android_light_sq_ctn
        ), contentDescription = "Continue with Google",
        modifier = Modifier.clickable {
            TODO()
        }
    )
}