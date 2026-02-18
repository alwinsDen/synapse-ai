package com.alwinsden.dino.botInterface.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun InitializedUiState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontFamily = FontLibrary.ebGaramond(),
                        fontSize = 30.sp
                    ),
                ) {
                    append("Hello Alwin.\n")
                }
                withStyle(SpanStyle(fontFamily = FontLibrary.ebGaramond())) {
                    append("let's begin?")
                }
            }, fontSize = 40.sp, textAlign = TextAlign.Center
        )
    }
}

data class ModelSelectionParamDetails(
    val name: String,
    val description: String,
    val incomingType: String
)

@Composable
fun ModelSelectionRadioMenu(
    onClick: (incomingNewType: String) -> Unit,
    state: ModelSelectionParamDetails,
    currentSelection: String
) {
    Row(
        modifier = Modifier
            .height(65.dp)
            .selectable(
                role = Role.RadioButton,
                selected = (state.incomingType == currentSelection),
                onClick = {
                    onClick(state.incomingType)
                }
            )
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = state.name, style = defaultFontStyle(
                    DefaultFontStylesDataClass()
                )
            )
            Text(
                text = state.description, style = defaultFontStyle(
                    DefaultFontStylesDataClass(
                        reduceFromDefault = 2,
                        fontFamily = FontLibrary.ebGaramond()
                    )
                )
            )
        }
        RadioButton(
            selected = (state.incomingType == currentSelection),
            //it's recommended to keep this null and let .selectable handle onclick for better accessibility
            onClick = null
        )
    }

}