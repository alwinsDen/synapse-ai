package com.alwinsden.dino.botInterface.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.PageDefaults
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import com.alwinsden.dino.utilities.UI.listModelDefinitions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A composable text input field for interacting with AI bot models.
 *
 * Displays a multi-line text field with the following features:
 * - Image attachment button (placeholder, currently no-op)
 * - Expandable text input area with scrolling support (max height: 200dp)
 * - Send button that changes color based on input state
 * - AI model selection button that opens a bottom sheet modal
 *
 * The text field includes:
 * - Placeholder text: "ask Dino, anything ✨"
 * - Gray background (0xffF2F2F2) with rounded corners (10dp)
 * - Dynamic send button color: gray when empty, green (0xff23D76E) when text is present
 *
 * The model selection bottom sheet allows users to choose between different AI models
 * defined in [ModelDefinitions], with the default selection being Gemini.
 *
 * @param mode Optional display mode filter. When null or matching [PageDefaults.botTextDefault],
 *             the text field is rendered. Other modes result in no UI being displayed.
 *
 * @see ModelDefinitions for available AI model options
 * @see PageDefaults for mode configuration
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BotTextField(mode: String? = null) {
    val defaultFieldText = "ask Synapse, anything ✨"
    val defaultTextFieldValue = rememberTextFieldState("")
    val textFieldScrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    val routineScope = rememberCoroutineScope()
    val controlBottomSheetStatus = remember { mutableStateOf<Boolean>(false) }
    val defaultModelSelection = remember { mutableStateOf<String>(ModelDefinitions.CLAUDE.name) }
    if ((mode === null || mode == PageDefaults.botTextDefault)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(shape = RoundedCornerShape(10.dp), color = Color.Transparent, width = 1.dp)
                .background(color = Color(0xffF2F2F2)),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Attach images",
                    tint = Color(0xff828282)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                BasicTextField(
                    state = defaultTextFieldValue,
                    textStyle = defaultFontStyle(
                        DefaultFontStylesDataClass(
                            colorInt = 0xff000000,
                        )
                    ),
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .verticalScroll(textFieldScrollState)
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                )
                if (defaultTextFieldValue.text.toString().isEmpty()) {
                    Text(
                        text = defaultFieldText,
                        style = defaultFontStyle(
                            DefaultFontStylesDataClass(
                                colorInt = 0xff958282,
                                fontFamily = FontLibrary.inter()
                            )
                        ),
                        modifier = Modifier.padding(vertical = 10.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send query",
                    tint = Color(if (defaultTextFieldValue.text.isBlank()) 0xff888888 else 0xff23D76E)
                )
            }
        }
        Spacer(Modifier.height(3.dp))
        Box(
            Modifier.fillMaxWidth(.9f)
        ) {
            ElevatedButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    controlBottomSheetStatus.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffF2F2F2),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = defaultModelSelection.value
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = defaultFontStyle(
                        DefaultFontStylesDataClass()
                    )
                )
            }
        }
        if (controlBottomSheetStatus.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    routineScope.launch {
                        if (sheetState.isVisible)
                            sheetState.hide()
                    }.invokeOnCompletion { controlBottomSheetStatus.value = false }
                },
                sheetState = sheetState
            ) {
                Column(
                    //this is necessary for screen readers.
                    modifier = Modifier.selectableGroup()
                ) {
                    listModelDefinitions.forEach { state ->
                        ModelSelectionRadioMenu(
                            state = state,
                            onClick = { incomingNewType ->
                                defaultModelSelection.value = incomingNewType
                                routineScope.launch {
                                    delay(200)
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    controlBottomSheetStatus.value = false
                                }
                            },
                            currentSelection = defaultModelSelection.value
                        )
                    }
                }
            }
        }
    }
}
