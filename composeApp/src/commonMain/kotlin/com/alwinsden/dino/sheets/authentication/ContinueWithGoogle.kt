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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.requestManager.RequestManager
import com.alwinsden.dino.requestManager.get.createNonce
import com.alwinsden.dino.utilities.UI.ClientKtorConfiguration
import com.alwinsden.dino.utilities.UI.Defaults
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
            .fillMaxSize(),
    ) {
        Box(Modifier.align(Alignment.Center)) {
            Column {
                Text(
                    "Project Dino*",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(Modifier.height(5.dp))
                Column {
                    Text(
                        "continue with",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    ClickableContinueWithGoogle(nonce)
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
    }
}