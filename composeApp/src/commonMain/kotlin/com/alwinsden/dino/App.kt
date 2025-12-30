package com.alwinsden.dino

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.alwinsden.dino.sheets.authentication.ContinueWithGoogle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        ContinueWithGoogle()
    }
}