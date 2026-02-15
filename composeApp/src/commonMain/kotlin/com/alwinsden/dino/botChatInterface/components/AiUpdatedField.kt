package com.alwinsden.dino.botChatInterface.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.ic_dino_chat_sq
import org.jetbrains.compose.resources.painterResource

@Composable
fun AiUpdatedField(maxWidth: Dp) {
    Box(
        Modifier.padding(start = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.ic_dino_chat_sq
                ), contentDescription = null
            )
            Spacer(Modifier.width(5.dp))
            Text(
                "synapse", style = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        reduceFromDefault = 2
                    )
                )
            )
        }
        Text(
            modifier = Modifier.padding(top = 25.dp, bottom = 25.dp),
            style = defaultFontStyle(
                DefaultFontStylesDataClass()
            ),
            text = "I'm doing excellent, thank you for asking! I'm powered up and ready to dive into whatever you've got on your mind today."
        )
    }
}