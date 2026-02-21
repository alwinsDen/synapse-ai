package com.alwinsden.dino.startup.components

import androidx.compose.runtime.Composable

@Composable
actual fun UiConfirmModal(
    message: String,
    userState: (confirmed: Boolean) -> Unit,
    showCancel: Boolean?
) {
}