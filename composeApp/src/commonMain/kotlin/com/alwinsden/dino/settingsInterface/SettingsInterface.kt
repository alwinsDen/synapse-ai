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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
            .background(color = Color(0xff000000))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "User settings", tint = Color.White)
            Text(
                text = "Settings",
                style = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        fontSize = 28.sp,
                        fontFamily = FontLibrary.monserrat()
                    )
                )
            )
            Spacer(Modifier.weight(1f, true))
            IconButton(
                onClick = {
                    navController?.popBackStack()
                }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close settings",
                    tint = Color.White
                )
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
                    DefaultFontStylesDataClass()
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .height(40.dp)
                    .background(color = Color.DarkGray)
                    .border(
                        width = 1.dp, shape = RoundedCornerShape(
                            10.dp
                        ), color = Color(0xff666666)
                    )
                    .padding(10.dp)
                ,
                lineLimits = TextFieldLineLimits.SingleLine,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "*Claude token is stored locally. Will reset on clearing cache or app deletion.",
            style = defaultFontStyle(
                DefaultFontStylesDataClass(
                    reduceFromDefault = 2
                )
            )
        )
    }
}
