package com.alwinsden.dino.historyDrawer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun HistoryDrawer(
    innerContent: @Composable () -> Unit,
    rememberDrawerState: DrawerState,
    historyArray: Sequence<String>
) {
    ModalNavigationDrawer(
        drawerState = rememberDrawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Past conversations",
                        style = defaultFontStyle(
                            DefaultFontStylesDataClass(
                                fontSize = 28.sp,
                                fontFamily = FontLibrary.ebGaramond()
                            )
                        )
                    )
                }
                Spacer(Modifier.height(2.dp))
                historyArray.forEach { it ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = it,
                                style = defaultFontStyle(
                                    DefaultFontStylesDataClass(
                                        colorInt = 0xff4F4F4F
                                    )
                                )
                            )
                        },
                        selected = false,
                        onClick = {
                            //
                        },
                        modifier = Modifier.height(50.dp),
                    )
                }

            }
        }
    ) {
        innerContent()
    }
}