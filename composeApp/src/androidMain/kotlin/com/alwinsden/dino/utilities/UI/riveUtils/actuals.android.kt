package com.alwinsden.dino.utilities.UI.riveUtils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.rive.Rive
import app.rive.RiveFileSource
import app.rive.rememberRiveFile
import app.rive.rememberRiveWorker
import app.rive.Result as RiveResult

@SuppressLint("LocalContextResourcesRead")
@Composable
actual fun GenericRiveAnimation(modifier: Modifier, riveBackgroundColor: String, animatedFileSource: String) {
    val context = LocalContext.current
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val riveWorker = rememberRiveWorker()
    val resId = context.resources.getIdentifier(animatedFileSource, "raw", context.packageName)
    val riveFile = rememberRiveFile(
        RiveFileSource.RawRes.from(resId),
        riveWorker
    )
    when (riveFile) {
        is RiveResult.Loading -> {}
        is RiveResult.Error -> {}
        is RiveResult.Success -> {
            Rive(riveFile.value, modifier = modifier)
        }
        else -> {}
    }
}