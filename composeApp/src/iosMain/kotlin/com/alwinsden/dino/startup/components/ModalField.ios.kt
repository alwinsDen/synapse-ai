package com.alwinsden.dino.startup.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.*

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UiConfirmModal(message: String, userState: (confirmed: Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        val alert = UIAlertController.alertControllerWithTitle(
            title = "Leaving?",
            message = message,
            preferredStyle = UIAlertControllerStyleAlert,
        )
        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "Yes",
                style = UIAlertActionStyleDefault,
                handler = { _ -> userState(true) },
            )
        )
        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "Cancel",
                style = UIAlertActionStyleCancel,
                handler = { _ -> userState(false) },
            )
        )
        rootViewController?.presentViewController(alert, animated = true, completion = null)
    }
}
