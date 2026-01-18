package com.alwinsden.dino.botInterface.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.FontLibrary
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun InitializedUiState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontFamily = FontLibrary.ebGaramond(), color = Color(0xff23D76E))) {
                    append("dino ")
                }
                withStyle(SpanStyle(fontFamily = FontLibrary.ebGaramond())) {
                    append("AI")
                }
            }, fontSize = 60.sp, textAlign = TextAlign.Center
        )
        Text(
            "A new day! How is your day going so far?",
            textAlign = TextAlign.Center,
            fontFamily = FontLibrary.ebGaramond(),
            modifier = Modifier.width(220.dp),
            fontSize = 16.sp,
        )
    }
}
