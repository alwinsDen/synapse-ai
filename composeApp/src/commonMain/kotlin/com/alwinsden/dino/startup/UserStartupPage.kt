package com.alwinsden.dino.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alwinsden.dino.authentication.ClickableContinueWithApple
import com.alwinsden.dino.authentication.ClickableContinueWithGoogle
import com.alwinsden.dino.authentication.components.rememberGoogleAuthProvider
import com.alwinsden.dino.requestManager.ApiState
import com.alwinsden.dino.requestManager.SubmitGoogleLoginViewModel
import com.alwinsden.dino.startup.components.UiConfirmModal
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import com.alwinsden.dino.utilities.UI.riveUtils.GenericRiveAnimation
import com.alwinsden.dino.utilities.UI.symbols.alwinsden.AlwinsDenIcon
import kotlinx.coroutines.launch

@Composable
fun UserStartupPage(navController: NavController? = null) {
    data class ErrorModalState(
        var state: Boolean,
        var errorMessage: String
    )

    val googleLoginViewModel = viewModel { SubmitGoogleLoginViewModel() }
    val scope = rememberCoroutineScope()
    val authProvider = rememberGoogleAuthProvider()
    val alertDialogState = remember {
        mutableStateOf(
            ErrorModalState(
                state = false,
                errorMessage = "Something went wrong."
            )
        )
    }
    val logoutModalState = remember { mutableStateOf(false) }
    val googleAuthState = googleLoginViewModel.googleAuthResponse.collectAsState()

    LaunchedEffect(googleAuthState.value) {
        when (val state = googleAuthState.value) {
            is ApiState.Loading -> {}
            is ApiState.Success -> {}
            is ApiState.Error -> {
                alertDialogState.value = ErrorModalState(
                    state = true,
                    errorMessage = state.message
                )
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .background(Color(0xffF3DB00))
            .statusBarsPadding()
            .fillMaxSize(),
    ) {
        Box(
            Modifier.align(Alignment.TopStart)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    logoutModalState.value = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = ""
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.30f)
                        .aspectRatio(150f / 60f)
                ) {
                    GenericRiveAnimation(
                        modifier = Modifier.fillMaxSize()
                            .background(color = Color.Transparent),
                        riveBackgroundColor = "#F3DB00",
                        animatedFileSource = "toggle"
                    )
                }
            }

        }
        if (logoutModalState.value) {
            UiConfirmModal("Are you sure you want to log out of Google account?", { confirmState ->
                if (confirmState) {
                    scope.launch {
                        authProvider.logoutFromGoogle()
                    }.invokeOnCompletion {
                        logoutModalState.value = false
                        println("logged out")
                    }
                } else {
                    logoutModalState.value = false
                }
            })
        }
        Box(Modifier.align(Alignment.Center)) {
            Column(
                modifier = Modifier
                    .width(320.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text = "Synapse AI",
                    style = defaultFontStyle(
                        DefaultFontStylesDataClass(
                            fontSize = 53.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    /*this was added cuz we wanted the fontsize to cover the width of container*/
                    /*this comes from foundation library and is pretty new.*/
                    autoSize = TextAutoSize.StepBased(maxFontSize = 200.sp),
                )
                Text(
                    "continue with",
                    style = defaultFontStyle(
                        DefaultFontStylesDataClass(
                            fontSize = 30.sp,
                            colorInt = 0xff000000,
                            fontFamily = FontLibrary.monserrat()
                        )
                    )
                )
                Spacer(Modifier.height(5.dp))
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ClickableContinueWithGoogle(handleReceivedGoogleTokenId = { googleTokenId, nonce ->
                        googleLoginViewModel.login(googleTokenId, nonce)
                    })
                    ClickableContinueWithApple()
                }
            }
        }
        if (alertDialogState.value.state) {
            AlertDialog(
                onDismissRequest = {
                    googleLoginViewModel.resetState()
                    alertDialogState.value = alertDialogState.value.copy(
                        state = false
                    )
                },
                title = { Text("Network error") },
                text = { Text(alertDialogState.value.errorMessage) },
                confirmButton = {
                    //
                },
            )
        }
        Box(
            Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            AlwinsDenIcon()
        }
    }
}