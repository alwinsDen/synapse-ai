package com.alwinsden.dino.historyDrawer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alwinsden.dino.navigation.SettingsNavigation
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle

@Composable
fun HistoryDrawer(
    innerContent: @Composable () -> Unit,
    rememberDrawerState: DrawerState,
    historyArray: Sequence<String>,
    navigationController: NavController? = null
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
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
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
                    Spacer(Modifier.height(10.dp))
                    Spacer(
                        Modifier.height(1.dp)
                            .fillMaxWidth(.9f)
                            .border(
                                color = Color(0xff000000),
                                width = 2.dp
                            )
                    )
                    Spacer(Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        historyArray.forEach { it ->
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        text = it,
                                        modifier = Modifier.weight(1f, fill = false),
                                        style = defaultFontStyle(
                                            DefaultFontStylesDataClass(
                                                colorInt = 0xff4F4F4F,
                                                fontFamily = FontLibrary.ebGaramond(),
                                                fontSize = 20.sp
                                            )
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                selected = false,
                                onClick = {
                                    //
                                },
                                modifier = Modifier.height(50.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp)

                        ) {
                            Row() {
                                Text(
                                    text = "alwintv5018@gmail.com", style =
                                        defaultFontStyle(
                                            DefaultFontStylesDataClass(
                                                fontFamily = FontLibrary.ebGaramond(),
                                                fontSize = 20.sp
                                            )
                                        )
                                )
                            }
                            IconButton(onClick = {
                                navigationController?.navigate(SettingsNavigation)
                            }) {
                                Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    ) {
        innerContent()
    }
}