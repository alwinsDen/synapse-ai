package com.alwinsden.dino.botChatInterface.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.botChatInterface.previews.chat_samples_BotChatInterface
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun AiUpdatedField(maxWidth: Dp) {
    Box(
        Modifier.padding(horizontal = 20.dp)
    ) {
        Column(
        ) {
            Text(
                modifier = Modifier.padding(top = 15.dp),
                style = defaultFontStyle(
                    DefaultFontStylesDataClass()
                ),
                lineHeight = 25.sp,
                text = chat_samples_BotChatInterface.model_response
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        //
                    },
                ) {
                    Icon(imageVector = Icons.Default.ThumbUpOffAlt, contentDescription = null, tint = Color.White)
                }
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.ThumbDownOffAlt, contentDescription = null, tint = Color.White)
                }
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.CopyAll, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
