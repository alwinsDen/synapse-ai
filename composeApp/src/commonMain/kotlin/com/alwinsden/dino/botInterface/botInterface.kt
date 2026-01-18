package com.alwinsden.dino.botInterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alwinsden.dino.botInterface.components.BotTextField
import com.alwinsden.dino.botInterface.components.InitializedUiState
import com.alwinsden.dino.utilities.UI.IncomingFontStylesDataClass
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
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize(),
        ) {
            InitializedUiState()
            BotTextField()
            Text(
                text = "*images can be uploaded, but are not analysed.",
                style = defaultFontStyle(
                    IncomingFontStylesDataClass()
                )
            )
        }
    }
}