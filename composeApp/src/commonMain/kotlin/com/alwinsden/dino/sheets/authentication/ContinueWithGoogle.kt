package com.alwinsden.dino.sheets.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.android_light_sq_ctn
import dino.composeapp.generated.resources.dino_corner
import org.jetbrains.compose.resources.painterResource

//automated Credential Manager
@Composable
expect fun TriggerAutoSignIn(): Unit

@Composable
fun ContinueWithGoogle() {
    TriggerAutoSignIn()
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
                    Image(
                        painter = painterResource(
                            resource = Res.drawable.android_light_sq_ctn
                        ), contentDescription = "Continue with Google"
                    )
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