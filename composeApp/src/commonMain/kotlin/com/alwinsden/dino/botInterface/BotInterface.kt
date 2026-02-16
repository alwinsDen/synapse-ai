package com.alwinsden.dino.botInterface

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alwinsden.dino.botInterface.components.BotTextField
import com.alwinsden.dino.botInterface.components.InitializedUiState
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.PageDefaults
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/*
* NAVIGATION: BotInterface fot the main
* Bot interaction page.
* */
@Preview(showBackground = true)
@Composable
fun BotInterface(mode: String? = null) {
    if ((mode === null || mode == PageDefaults.botTextDefault)) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize(),
        ) {
            Box(Modifier.align(Alignment.Center).offset(y = -(20.dp))) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    InitializedUiState()
                    Spacer(Modifier.height(20.dp))
                    BotTextField()
                }
            }
            Text(
                text = "*images can be uploaded, but are not saved.",
                style = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        fontFamily = FontLibrary.inter(),
                        reduceFromDefault = 2
                    )
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}