package com.alwinsden.dino.startup.components

import androidx.compose.runtime.Composable

@Composable
expect fun UiConfirmModal(message: String, userState: (confirmed: Boolean) -> Unit)