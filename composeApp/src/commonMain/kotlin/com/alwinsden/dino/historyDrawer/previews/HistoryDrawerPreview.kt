package com.alwinsden.dino.historyDrawer.previews

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import com.alwinsden.dino.historyDrawer.HistoryDrawer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun HistoryDrawerPreview() {
    HistoryDrawer(
        {
            Text(text = "Past conversations")
        },
        rememberDrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
        historyArray = sequenceOf(
            "Choreographer implicitly registered..",
            "composeApp:compileDebugJava..."
        )
    )
}