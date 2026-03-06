package com.alwinsden.dino.utilities.UI.riveUtils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.alwinsden.dino.riveViewControllerIos

@Composable
actual fun MartyAnimation(modifier: Modifier) {
    val factory = checkNotNull(riveViewControllerIos)
    UIKitViewController(
        factory = {factory.createMartyView()},
        modifier = modifier,
    )
}