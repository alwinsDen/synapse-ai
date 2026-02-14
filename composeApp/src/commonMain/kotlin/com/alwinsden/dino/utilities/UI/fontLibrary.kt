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

data class DefaultFontStylesDataClass(
    val fontSize: TextUnit? = null,
    val fontFamily: FontFamily? = null,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val colorInt: Long? = null,
    val reduceFromDefault: Int? = null
)

/*
* define font styles
* */

@Composable
fun defaultFontStyle(incomingStyles: DefaultFontStylesDataClass): TextStyle {
    return TextStyle(
        fontFamily = incomingStyles.fontFamily ?: FontLibrary.inter(),
        fontWeight = incomingStyles.fontWeight ?: FontWeight.Normal,
        fontStyle = incomingStyles.fontStyle ?: FontStyle.Normal,
        fontSize = incomingStyles.fontSize ?: (16 - (incomingStyles.reduceFromDefault ?: 0)).sp,
        color = Color(incomingStyles.colorInt ?: 0xff000000),
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

    @Composable
    fun inter(): FontFamily {
        return FontFamily(
            Font(
                resource = Res.font.inter_18pt_regular,
                FontWeight.Normal
            ),
            Font(
                resource = Res.font.inter_18pt_medium,
                FontWeight.Medium
            ),
            Font(
                resource = Res.font.inter_18pt_italic,
                FontWeight.Normal,
                style = FontStyle.Italic,
            ),
            Font(
                resource = Res.font.inter_18pt_thin,
                FontWeight.Thin,
            ),
            Font(
                resource = Res.font.inter_18pt_semibold,
                FontWeight.SemiBold,
            ),
            Font(
                resource = Res.font.inter_18pt_bold,
                FontWeight.Bold,
            ),
        )
    }
}