package com.alwinsden.dino.utilities.UI.riveUtils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.alwinsden.dino.riveViewControllerIos

@Composable
actual fun GenericRiveAnimation(modifier: Modifier, riveBackgroundColor: String,animatedFileSource: String) {
    val factory = checkNotNull(riveViewControllerIos)
    UIKitViewController(
        factory = {factory.createRiveView(riveBackgroundColor, animatedFileSource) },
        modifier = modifier,
    )
}