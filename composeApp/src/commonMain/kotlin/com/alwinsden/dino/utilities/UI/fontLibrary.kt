package com.alwinsden.dino.utilities.UI

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dino.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font


/*
* define data classes
* */
data class IncomingFontStylesDataClass(
    val fontSize: TextUnit? = null,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val colorInt: Long? = null,
)

/*
* define font styles
* */

@Composable
fun defaultFontStyle(incomingStyles: IncomingFontStylesDataClass): TextStyle {
    return TextStyle(
        fontFamily = FontLibrary.ebGaramond(),
        fontWeight = incomingStyles.fontWeight ?: FontWeight.Normal,
        fontStyle = incomingStyles.fontStyle ?: FontStyle.Normal,
        fontSize = incomingStyles.fontSize ?: 18.sp,
        color = Color(incomingStyles.colorInt ?: 0xff000000)
    )
}


/*
EBGaramond Font6
*/
object FontLibrary {
    @Composable
    fun ebGaramond(): FontFamily {
        return FontFamily(
            Font(
                resource = Res.font.ebgaramond_regular,
                FontWeight.Normal,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.ebgaramond_medium,
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                resource = Res.font.ebgaramond_bold,
                FontWeight.Bold,
                FontStyle.Normal
            ),
            Font(
                resource = Res.font.ebgaramond_italic,
                FontWeight.Normal,
                FontStyle.Italic
            )
        )
    }
}