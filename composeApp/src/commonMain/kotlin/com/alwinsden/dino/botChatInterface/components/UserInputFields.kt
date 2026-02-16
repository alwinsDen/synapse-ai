package com.alwinsden.dino.botChatInterface.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun UserCreatedField(maxWidth: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(end = 10.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "I am doing great. How are you?",
                lineHeight = 25.sp,
                style = defaultFontStyle(
                    DefaultFontStylesDataClass()
                ), modifier = Modifier
                    .widthIn(max = maxWidth)
                    .clip(
                        RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 0.dp,
                            bottomStart = 15.dp,
                            bottomEnd = 15.dp
                        )
                    )
                    .background(color = Color(0xffF2F2F2))
                    .padding(8.dp)

            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

