package org.beatonma.commons.theme.color

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Resolve the correct color for the active theme.
 */
@Composable
internal fun resolveColor(color: Themed): Color = color.color

/**
 * Clone of [resolveColor] for API consistency - consumer implementation should not need to know
 * whether a color is themed or not.
 */
internal fun resolveColor(color: Color): Color = color

internal interface Themed {
    val light: Color
    val dark: Color

    val color: Color
        @Composable get() = when {
            MaterialTheme.colors.isLight -> light
            else -> dark
        }
}

private fun themed(light: Color, dark: Color) = object : Themed {
    override val light: Color = light
    override val dark: Color = dark
}

internal object CommonsColor {
    val Background: Themed = themed(Color(0xff_EB_E9_E8), Color(0xff_11_11_11))
    val Surface: Themed = themed(Color.White, Color(0xff_22_22_22))

    val TextPrimary: Themed = themed(Text.PrimaryDark, Text.PrimaryLight)
    val TextSecondary: Themed = themed(Text.SecondaryDark, Text.SecondaryLight)
    val TextTertiary: Themed = themed(Text.TertiaryDark, Text.TertiaryLight)
    val Primary: Themed = themed(Accent.Purple, Accent.Mint)
    val PrimaryVariant: Themed = themed(Accent.DarkPurple, Accent.DarkMint)
    val Secondary: Themed = themed(Accent.Mint, Accent.Purple)
    val SecondaryVariant: Themed = themed(Accent.DarkMint, Accent.DarkPurple)
    val WarningSurface: Color = Color(0xff_cd_2d_22)
    val OnWarningSurface: Color = Text.PrimaryLight
    val SearchBar: Color = Color(0xff_48406e)
    val OnSearchBar: Color = Text.PrimaryLight
    val SelectedSurface: Themed = themed(Color(0xff_f1ebfc), Color(0xff_b0d6c6))
    val OnSelectedSurface: Color = Text.PrimaryDark

    val Positive = Color(0xff_4C_AF_50)
    val Negative = Color(0xff_F4_43_36)
    val ModalScrim = Color(0xbc_00_00_00)

    object Accent {
        val Mint = Color(0xff_96_DC_BE)
        val DarkMint = Color(0xff_00_54_34)

        val Purple = Color(0xff_5F_2D_B4)
        val DarkPurple = Color(0xff_77_72_85)
    }

    object Text {
        val PrimaryDark = Color(0xD8_00_00_00)
        val PrimaryLight = Color(0xD8_FF_FF_FF)
        val SecondaryDark = Color(0x89_00_00_00)
        val SecondaryLight = Color(0x89_FF_FF_FF)
        val TertiaryDark = Color(0x3A_00_00_00)
        val TertiaryLight = Color(0x3A_FF_FF_FF)
    }

    object Graph {
        val Primary = listOf(
            MaterialRed700,
            MaterialPink700,
            MaterialPurple700,
            MaterialBlue700,
            MaterialTeal700,
            MaterialGreen700,
            MaterialAmber700,
            MaterialDeepOrange700,
            MaterialGrey700,
            MaterialBlueGrey700,
        )

        val Secondary = listOf(
            MaterialRed200,
            MaterialPink200,
            MaterialPurple200,
            MaterialBlue200,
            MaterialTeal200,
            MaterialGreen200,
            MaterialAmber200,
            MaterialDeepOrange200,
            MaterialGrey400,
            MaterialBlueGrey200,
        )
    }
}
