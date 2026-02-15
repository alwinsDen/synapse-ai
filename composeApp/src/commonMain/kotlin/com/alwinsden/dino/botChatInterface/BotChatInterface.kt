package com.alwinsden.dino.botChatInterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.alwinsden.dino.botChatInterface.components.AiUpdatedField
import com.alwinsden.dino.botChatInterface.components.UserCreatedField
import com.alwinsden.dino.botInterface.components.BotTextField
import com.alwinsden.dino.historyDrawer.HistoryDrawer
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import kotlinx.coroutines.launch

/**
 * The main chat interface screen for conversing with AI bot models.
 *
 * This composable creates a full-screen chat interface with three main sections:
 *
 * ## Layout Structure
 *
 * ### Top Bar (Fixed)
 * - Settings button (left)
 * - Date display showing "17th January 2026" (center-left)
 * - Logout button with power icon (right)
 * - History button (right)
 * - Background: Light gray (0xfffafafa)
 *
 * ### Chat Content (Scrollable)
 * - Vertically scrollable column containing alternating message bubbles
 * - [UserCreatedField] components for user messages
 * - [AiUpdatedField] components for AI responses
 * - Message width constrained to 70% of screen width for readability
 * - Disclaimer text at bottom: "Verify the output generated." (gray, small font)
 * - Extra bottom padding (120dp) to prevent content from being hidden behind input field
 *
 * ### Bottom Input Area (Fixed)
 * - [BotTextField] component for user input
 * - Elevated appearance with drop shadow (color: 0xff98D6B2, radius: 5dp, spread: 5dp)
 * - Rounded top corners (20dp radius)
 * - White background (0xffffffff)
 * - Handles navigation bar padding automatically
 *
 * ## Accessibility
 * - Properly handles status bar and navigation bar padding on different devices
 * - Icon buttons include content descriptions for screen readers
 * - Scrollable content ensures all messages are accessible
 *
 * @see BotTextField for the input field component
 * @see UserCreatedField for user message bubble styling
 * @see AiUpdatedField for AI response bubble styling
 */
@Composable
fun BotChatInterface() {
    val verticalScrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    HistoryDrawer(
        {
            BoxWithConstraints(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .background(color = Color(0xffffffff))
            ) {
                val mxWidth = maxWidth * .7f
                Column(
                    Modifier.verticalScroll(state = verticalScrollState)
                ) {
                    Spacer(Modifier.height(50.dp))
                    UserCreatedField(maxWidth = mxWidth)
                    AiUpdatedField(maxWidth = mxWidth)
                    Text(
                        "Verify the output generated.", textAlign = TextAlign.Center,
                        style = defaultFontStyle(
                            DefaultFontStylesDataClass(
                                colorInt = 0xff999999,
                                fontFamily = FontLibrary.ebGaramond()
                            )
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Spacer(
                        Modifier
                            .navigationBarsPadding()
                            .height(120.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .background(color = Color(0xffffffff))
                ) {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
                    }
                    Text(
                        text = "17th January 2026", textAlign = TextAlign.Center, style = defaultFontStyle(
                            DefaultFontStylesDataClass()
                        )
                    )
                    Row(
                        modifier = Modifier.weight(1f, true),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = "Logout app")
                        }
                        IconButton(onClick = {
                            coroutineScope.launch() {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.History, contentDescription = "History of conversations")
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .dropShadow(
                            shape = RoundedCornerShape(20.dp),
                            shadow = Shadow(
                                radius = 5.dp,
                                spread = 5.dp,
                                color = Color(0xfff0f0f0),
                                offset = DpOffset(x = 0.dp, (3).dp)
                            )
                        )
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(Color(0xffffffff))
                        .padding(top = 15.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BotTextField()
                }
            }
        },
        rememberDrawerState = drawerState,
        historyArray = sequenceOf("testElement")
    )
}