package com.alwinsden.dino.startup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.authentication.ClickableContinueWithApple
import com.alwinsden.dino.authentication.ClickableContinueWithGoogle
import com.alwinsden.dino.authentication.handleReceivedGoogleTokenId
import com.alwinsden.dino.requestManager.RequestManager
import com.alwinsden.dino.requestManager.get.createNonce
import com.alwinsden.dino.utilities.UI.*
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.ic_alwinsden_black_rec
import org.jetbrains.compose.resources.painterResource
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
                    "Project Synapse",
                    style = defaultFontStyle(
                        incomingStyles = DefaultFontStylesDataClass(
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Normal,
                            colorInt = 0xff000000,
                            fontFamily = FontLibrary.ebGaramond()
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
//        Box(Modifier.align(Alignment.BottomEnd)) {
//            Image(
//                painter = painterResource(
//                    resource = Res.drawable.ic_dino_corner_sq,
//                ),
//                contentDescription = "Corner logo for the ",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth(0.7f)
//            )
//        }
        Box(
            Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        resource = Res.drawable.ic_alwinsden_black_rec
                    ),
                    contentDescription = "AlwinsDen logo",
                    Modifier.width(40.dp)
                )
                Text(
                    text = "alwinsden.com",
                    style = defaultFontStyle(
                        DefaultFontStylesDataClass(
                            fontFamily = FontLibrary.ebGaramond(),
                            fontSize = 18.sp,
                        )
                    ),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        //ErrorPopUp("lorem aisji  oajsid asiodio  asdjaijir˳")
    }
}