package com.alwinsden.dino.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alwinsden.dino.authentication.ClickableContinueWithApple
import com.alwinsden.dino.authentication.ClickableContinueWithGoogle
import com.alwinsden.dino.authentication.components.rememberGoogleAuthProvider
import com.alwinsden.dino.requestManager.StartUpLaunchViewModel
import com.alwinsden.dino.requestManager.utils.handleReceivedGoogleTokenId
import com.alwinsden.dino.startup.components.UiConfirmModal
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import com.alwinsden.dino.utilities.UI.symbols.alwinsden.AlwinsDenIcon
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UserStartupPage() {
    val vm = viewModel { StartUpLaunchViewModel() }
    val scope = rememberCoroutineScope()
    val authProvider = rememberGoogleAuthProvider()
    val logoutModalState = remember { mutableStateOf(false) }
    val nonce = vm.nonce.collectAsState()
    Box(
        modifier = Modifier
            .background(Color(0xffF3DB00))
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Box(
            Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(onClick = {
                logoutModalState.value = true
            }) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = ""
                )
            }
        }
        if (logoutModalState.value) {
            UiConfirmModal("Are you sure you want to log out of Google account?", { confirmState ->
                if (confirmState) {
                    scope.launch {
                        authProvider.logoutFromGoogle()
                    }.apply {
                        logoutModalState.value = false
                        println("logged out")
                    }
                } else {
                    logoutModalState.value = false
                }
            })
        }
        Box(Modifier.align(Alignment.Center)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = FontLibrary.ebGaramond(),
                            )
                        ) {
                            append("synapse ")
                        }
                        withStyle(SpanStyle(fontFamily = FontLibrary.ebGaramond())) {
                            append("ai")
                        }
                    },
                    fontSize = 45.sp,
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
                                colorInt = 0xff000000,
                                fontFamily = FontLibrary.ebGaramond()
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    ClickableContinueWithGoogle(nonce.value, handleReceivedGoogleTokenId = { googleTokenId ->
                        handleReceivedGoogleTokenId(googleTokenId)
                    })
                    Spacer(modifier = Modifier.height(5.dp))
                    ClickableContinueWithApple(nonce.value)
                }
            }
        }
        Box(
            Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            AlwinsDenIcon()
        }
    }
}