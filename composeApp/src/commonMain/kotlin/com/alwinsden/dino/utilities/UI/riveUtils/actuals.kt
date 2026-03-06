package com.alwinsden.dino.utilities.UI.riveUtils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun GenericRiveAnimation(modifier: Modifier, riveBackgroundColor: String, animatedFileSource: String): Unit