package com.alwinsden.dino.settingsInterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun SettingsInterface(navController: NavController? = null) {
    val claudeToken =
        rememberTextFieldState("")
    Column(
        Modifier
            .background(color = Color(0xffffffff))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "User settings")
            Text(
                text = "Settings",
                style = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        fontSize = 28.sp,
                        fontFamily = FontLibrary.ebGaramond()
                    )
                )
            )
            Spacer(Modifier.weight(1f, true))
            IconButton(
                onClick = {
                    navController?.popBackStack()
                }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close settings")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column {
            Text(
                "Claude API Token", style = defaultFontStyle(
                    DefaultFontStylesDataClass()
                )
            )
            Spacer(Modifier.height(10.dp))
            BasicTextField(
                state = claudeToken,
                textStyle = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        fontWeight = FontWeight.Thin,
                        colorInt = 0xff858585
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        width = 1.dp, shape = RoundedCornerShape(
                            10.dp
                        ), color = Color(0xff9c9c9c)
                    )
                    .padding(10.dp),
                lineLimits = TextFieldLineLimits.SingleLine,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "*Claude token is stored locally. Will reset on clearing cache or app deletion.",
            style = defaultFontStyle(
                DefaultFontStylesDataClass(
                    colorInt = 0xff9c9c9c,
                    reduceFromDefault = 2
                )
            )
        )
    }
}
