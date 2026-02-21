package com.alwinsden.dino.desktopModule.startup

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alwinsden.dino.utilities.UI.symbols.alwinsden.AlwinsDenIcon
import javax.swing.JFileChooser

@Composable
fun Startup() {
    var parentFolder by remember { mutableStateOf("") }
    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(vertical = 5.dp, horizontal = 10.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row() {
                Text(
                    text = "path: ",
                    color = Color.DarkGray
                )
                Text(
                    text = if (parentFolder == "") "Select a folder path" else parentFolder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = {
                        val chooser = JFileChooser()
                        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                        val result = chooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            parentFolder = chooser.selectedFile.absolutePath
                        }
                    },
                ) {
                    Text("Select Module")
                }
                if (parentFolder != "") {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()
                    Box {
                        Button(
                            onClick = {},
                            modifier = Modifier.hoverable(interactionSource),
                        ) {
                            Text("Add +")
                        }
                        if (isHovered) {
//                            Surface(
//                                modifier = Modifier
//                                    .align(Alignment.BottomStart)
////                                    .offset(y = (45).dp, x = -(0.dp))
//                                    .padding(0.dp),
//                            ) {
//                                Text(
//                                    text = "Add the submodules from the selected project here.",
//                                    modifier = Modifier
//                                )
//                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            AlwinsDenIcon()
        }
    }
}