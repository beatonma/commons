package org.beatonma.commons.theme.compose.color

import androidx.compose.ui.graphics.Color

object CommonsColor {
    object Accent {

        val Mint = Color(0x96DCBE)
        val DarkMint = Color(0x005434)

        val Purple = Color(0x5F2DB4)
        val DarkPurple = Color(0xA09CA9)
    }

    object Text {

        val PrimaryDark = Color(0xD8000000)
        val PrimaryLight = Color(0xD8FFFFFF)
        val SecondaryDark = Color(0x89000000)
        val SecondaryLight = Color(0x89FFFFFF)
        val TertiaryDark = Color(0x3A000000)
        val TertiaryLight = Color(0x3AFFFFFF)
    }

    interface Themed {

        val Background: Color
        val Surface: Color
        val SystemBar: Color
        val TextPrimary: Color
        val TextSecondary: Color
        val TextTertiary: Color

        val Secondary: Color
        val SecondaryVariant: Color
    }

    object Light : Themed {

        override val Background: Color = Color(0xEBE9E8)
        override val Surface: Color = Color.White
        override val SystemBar: Color = Background.copy(alpha = 0.4f)
        override val TextPrimary: Color = Text.PrimaryDark
        override val TextSecondary: Color = Text.SecondaryDark
        override val TextTertiary: Color = Text.TertiaryDark
        override val Secondary: Color = Accent.Purple
        override val SecondaryVariant: Color = Accent.DarkPurple
    }

    object Dark : Themed {

        override val Background: Color = Color(0xff111111)
        override val Surface: Color = Color(0xff222222)
        override val SystemBar: Color = Background.copy(alpha = 0.4f)
        override val TextPrimary: Color = Text.PrimaryLight
        override val TextSecondary: Color = Text.SecondaryLight
        override val TextTertiary: Color = Text.TertiaryLight
        override val Secondary: Color = Accent.Mint
        override val SecondaryVariant: Color = Accent.DarkMint
    }

    val Primary = Color(0xff444444)
    val PrimaryVariant = Color(0xff333333)

    val Positive = Color(0x4CAF50)
    val Negative = Color(0xF44336)

    val SearchBar = Color(0x373151)
    val DialogBackground = Color(0xbc000000)
}
