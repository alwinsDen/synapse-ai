package com.alwinsden.dino.utilities.UI.symbols.alwinsden

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alwinsden.dino.utilities.UI.DefaultFontStylesDataClass
import com.alwinsden.dino.utilities.UI.FontLibrary
import com.alwinsden.dino.utilities.UI.defaultFontStyle
import dino.composeapp.generated.resources.Res
import dino.composeapp.generated.resources.ic_alwinsden_black_rec
import org.jetbrains.compose.resources.painterResource

@Composable
fun AlwinsDenIcon() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                resource = Res.drawable.ic_alwinsden_black_rec
            ),
            contentDescription = "AlwinsDen logo",
            Modifier.width(40.dp)
        )
        Text(
            text = "alwinsden.com",
            style = defaultFontStyle(
                DefaultFontStylesDataClass(
                    fontFamily = FontLibrary.ebGaramond(),
                    fontSize = 18.sp,
                )
            ),
            modifier = Modifier.padding(5.dp)
        )
    }
}