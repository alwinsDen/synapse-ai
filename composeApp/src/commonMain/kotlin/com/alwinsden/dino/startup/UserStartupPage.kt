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
import androidx.navigation.NavController
import com.alwinsden.dino.authentication.ClickableContinueWithApple
import com.alwinsden.dino.authentication.ClickableContinueWithGoogle
import com.alwinsden.dino.authentication.components.rememberGoogleAuthProvider
import com.alwinsden.dino.requestManager.LoginState
import com.alwinsden.dino.requestManager.StartUpLaunchViewModel
import com.alwinsden.dino.requestManager.SubmitGoogleLoginViewModel
import com.alwinsden.dino.startup.components.UiConfirmModal
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import com.alwinsden.dino.utilities.UI.riveUtils.GenericRiveAnimation
import com.alwinsden.dino.utilities.UI.symbols.alwinsden.AlwinsDenIcon
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UserStartupPage(navController: NavController? = null) {
    val vm = viewModel { StartUpLaunchViewModel() }
    val googleLoginViewModel = viewModel { SubmitGoogleLoginViewModel() }
    val scope = rememberCoroutineScope()
    val authProvider = rememberGoogleAuthProvider()
    val logoutModalState = remember { mutableStateOf(false) }
    val nonce = vm.nonce.collectAsState()
    val googleAuthState = googleLoginViewModel.googleAuthResponse.collectAsState()
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
                        googleLoginViewModel.login(googleTokenId, nonce.value)
                    })
                    Spacer(modifier = Modifier.height(5.dp))
                    ClickableContinueWithApple(nonce.value)
                    Spacer(Modifier.height(10.dp))

                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .background(color = Color.Transparent)
                .height(150.dp)
                .width(150.dp)
        ) {
            GenericRiveAnimation(modifier = Modifier.fillMaxSize()
                .background(color = Color.Transparent),
                riveBackgroundColor = "#F3DB00",
                animatedFileSource = "toggle"
            )
        }
        Box(Modifier.padding(10.dp)) {
            when (val state = googleAuthState.value) {
                is LoginState.Loading -> {}
                is LoginState.Success -> {}
                is LoginState.Error -> {
                    Text(
                        "Error $state",
                        modifier = Modifier.background(color = Color(0xff000000)),
                        color = Color.Red
                    )
                }

                else -> {}
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