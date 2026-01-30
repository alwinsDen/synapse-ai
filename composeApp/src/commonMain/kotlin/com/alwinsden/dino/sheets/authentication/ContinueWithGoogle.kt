package com.alwinsden.dino.sheets.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.requestManager.RequestManager
import com.alwinsden.dino.requestManager.get.createNonce
import com.alwinsden.dino.utilities.UI.*
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.btn_android_id_rec
import dino.composeapp.generated.resources.btn_apple_id_rec
import dino.composeapp.generated.resources.ic_dino_corner_sq
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        authProvider.checkExistingCredentials()?.let { token ->
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

@Preview
@Composable
fun ContinueWithGoogle() {
    var nonce by remember { mutableStateOf(Defaults.default) }
    LaunchedEffect(Unit) {
        //nonce is fetched from the requested server.
        nonce = RequestManager(ClientKtorConfiguration()).createNonce()
    }
    Box(
        modifier = Modifier
            .background(Color(0xffF3DB00))
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Box(Modifier.align(Alignment.Center)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "Project Synapse",
                    style = defaultFontStyle(
                        incomingStyles = DefaultFontStylesDataClass(
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Normal,
                            colorInt = 0xff000000
                        )
                    ),
                    textAlign = TextAlign.Center,
                )
            }
            Box(
                Modifier.align(Alignment.Center)
            ) {
                Column {
                    Spacer(Modifier.height(65.dp))
                    Text(
                        "continue with",
                        style = defaultFontStyle(
                            DefaultFontStylesDataClass(
                                fontSize = 18.sp,
                                colorInt = 0xff000000
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    ClickableContinueWithGoogle(nonce, handleReceivedGoogleTokenId = { googleTokenId ->
                        handleReceivedGoogleTokenId(googleTokenId)
                    })
                    Spacer(modifier = Modifier.height(5.dp))
                    ClickableContinueWithApple(nonce)
                }
            }
        }
        Box(Modifier.align(Alignment.BottomEnd)) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.ic_dino_corner_sq,
                ),
                contentDescription = "Corner logo for the ",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
        //ErrorPopUp("lorem aisji  oajsid asiodio  asdjaijir˳")
    }
}