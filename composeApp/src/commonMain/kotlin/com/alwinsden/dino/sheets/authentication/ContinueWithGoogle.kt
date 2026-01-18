package com.alwinsden.dino.sheets.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.requestManager.RequestManager
import com.alwinsden.dino.requestManager.get.createNonce
import com.alwinsden.dino.utilities.UI.ClientKtorConfiguration
import com.alwinsden.dino.utilities.UI.Defaults
import com.alwinsden.dino.utilities.UI.IncomingFontStylesDataClass
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.dino_corner
import org.jetbrains.compose.resources.painterResource

//automated Credential Manager
@Composable
expect fun TriggerAutoSignIn(): Unit
expect suspend fun manualTriggerSignIn(): Unit

@Composable
expect fun ClickableContinueWithGoogle(nonce: String): Unit

@Composable
expect fun ClickableContinueWithApple(nonce: String): Unit

@Composable
fun ContinueWithGoogle() {
    var nonce by remember { mutableStateOf(Defaults.default) }
    LaunchedEffect(Unit) {
        //nonce is fetched from the requested server.
        nonce = RequestManager(ClientKtorConfiguration()).createNonce()
    }
    Box(
        modifier = Modifier
            .background(Color(0xff23D76E))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {
        Box(Modifier.align(Alignment.Center)) {
            Column {
                Text(
                    "Project Dino*",
                    color = Color.White,
                    style = defaultFontStyle(
                        incomingStyles = IncomingFontStylesDataClass(
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                )
                Spacer(Modifier.height(2.dp))
                Column {
                    Text(
                        "continue with",
                        color = Color.White,
                        style = defaultFontStyle(
                            IncomingFontStylesDataClass(
                                fontSize = 18.sp
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    ClickableContinueWithGoogle(nonce)
                    Spacer(modifier = Modifier.height(5.dp))
                    ClickableContinueWithApple(nonce)
                }
            }
        }
        Box(Modifier.align(Alignment.BottomEnd)) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.dino_corner,
                ),
                contentDescription = "Corner logo for the ",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
        //ErrorPopUp("lorem aisji  oajsid asiodio  asdjaijir˳")
    }
}