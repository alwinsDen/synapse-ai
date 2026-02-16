package com.alwinsden.dino.botChatInterface.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun AiUpdatedField(maxWidth: Dp) {
    Box(
        Modifier.padding(start = 10.dp)
    ) {
        Column(
        ) {
            Icon(
                imageVector = Icons.Default.Diamond,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 15.dp),
                style = defaultFontStyle(
                    DefaultFontStylesDataClass()
                ),
                lineHeight = 25.sp,
                text = "I'm doing excellent, thank you for asking! I'm powered up and ready to dive into whatever you've got on your mind today."
            )
            Row() {
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.ThumbUpOffAlt, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.ThumbDownOffAlt, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.CopyAll, contentDescription = null)
                }
            }
        }
    }
}
