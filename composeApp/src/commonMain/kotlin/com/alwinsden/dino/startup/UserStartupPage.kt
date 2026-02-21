package com.alwinsden.dino.startup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.alwinsden.dino.authentication.ClickableContinueWithApple
import com.alwinsden.dino.authentication.ClickableContinueWithGoogle
import com.alwinsden.dino.authentication.handleReceivedGoogleTokenId
import com.alwinsden.dino.requestManager.RequestManager
import com.alwinsden.dino.requestManager.get.createNonce
import com.alwinsden.dino.utilities.UI.*
import com.alwinsden.dino.utilities.UI.symbols.alwinsden.AlwinsDenIcon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UserStartupPage() {
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
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = FontLibrary.ebGaramond(),
                            )
                        ) {
                            append("synapsess ")
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
                    ClickableContinueWithGoogle(nonce, handleReceivedGoogleTokenId = { googleTokenId ->
                        handleReceivedGoogleTokenId(googleTokenId)
                    })
                    Spacer(modifier = Modifier.height(5.dp))
                    ClickableContinueWithApple(nonce)
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