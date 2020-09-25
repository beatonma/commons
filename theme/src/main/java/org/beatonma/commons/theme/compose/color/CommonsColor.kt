package org.beatonma.commons.theme.compose.color

import androidx.compose.ui.graphics.Color


object CommonsColor {
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

        override val Background: Color = Color(0xff_EB_E9_E8)
        override val Surface: Color = Color.White
        override val SystemBar: Color = Background.copy(alpha = 0.4f)
        override val TextPrimary: Color = Text.PrimaryDark
        override val TextSecondary: Color = Text.SecondaryDark
        override val TextTertiary: Color = Text.TertiaryDark
        override val Secondary: Color = Accent.Purple
        override val SecondaryVariant: Color = Accent.DarkPurple
    }

    object Dark : Themed {

        override val Background: Color = Color(0xff_11_11_11)
        override val Surface: Color = Color(0xff_22_22_22)
        override val SystemBar: Color = Background.copy(alpha = 0.4f)
        override val TextPrimary: Color = Text.PrimaryLight
        override val TextSecondary: Color = Text.SecondaryLight
        override val TextTertiary: Color = Text.TertiaryLight
        override val Secondary: Color = Accent.Mint
        override val SecondaryVariant: Color = Accent.DarkMint
    }

    val Primary = Color(0xff_44_44_44)
    val PrimaryVariant = Color(0xff_33_33_33)

    val Positive = Color(0xff_4C_AF_50)
    val Negative = Color(0xff_F4_43_36)

    val SearchBar = Color(0xff_37_31_51)
    val DialogBackground = Color(0xbc_00_00_00)

    object Political {

        val Parliament = Color(0xff_37_31_51)  // Purple
        val Westminster = Color(0xff_e8_e9_e8) // Off-white
        val Royal = Color(0xff_23_35_80)       // Blue

        object House {

            val Commons = Color(0xff_00_6e_46)
            val CommonsDark = Color(0xff_00_42_29)
            val CommonsDeep = Color(0xff_00_54_34)

            val Lords = Color(0xff_B5_09_38)
        }

        object Party {
            object Primary {

                val Default = Parliament
                val Alliance = Color(0xff_F4_C7_2E)
                val ChangeUk = Color.White
                val Conservative = Color(0xff_11_43_7D)
                val Dup = Color(0xff_43_06_7)
                val Green = Color(0xff_53_8C_6B)
                val Independent = Default
                val Labour = Color(0xff_C4_12_30)
                val LabourCoop = Color(0xff_71_1F_8E)
                val LibDem = Color(0xff_FD_BB_30)
                val PlaidCymru = Color(0xff_00_81_42)
                val Sdlp = Color(0xff_0B_69_4D)
                val SinnFein = Color(0xff_08_67_23)
                val Snp = Color(0xff_FF_95_D)
                val Ukip = Color(0xff_70_2F_85)
                val Uup = Color(0xff_D2_F5_D)
                val Speaker = Color(0xff_37_71_2C)

                fun all() = listOf(
                    Default,
                    Alliance,
                    ChangeUk,
                    Conservative,
                    Dup,
                    Green,
                    Independent,
                    Labour,
                    LabourCoop,
                    LibDem,
                    PlaidCymru,
                    Sdlp,
                    SinnFein,
                    Snp,
                    Ukip,
                    Uup,
                    Speaker,
                )
            }

            object Accent {

                val Default = MaterialRed300
                val Alliance = Color.Black
                val ChangeUk = Color(0xff_22_22_21)
                val Conservative = MaterialRed500
                val Dup = MaterialRed500
                val Green = MaterialLightGreen500
                val Independent = Default
                val Labour = MaterialAmber500
                val LabourCoop = MaterialRed500
                val Lib_dem = MaterialBlue500
                val PlaidCymru = MaterialAmber500
                val Sdlp = MaterialAmber500
                val SinnFein = MaterialDeepOrange500
                val Snp = Color.Black
                val Ukip = MaterialAmber500
                val Uup = MaterialBlue500
                val Speaker = MaterialBlue500
            }
        }

        object Vote {

            val Aye = Positive
            val No = Negative
            val Abstain = MaterialAmber600
            val DidNotVote = MaterialGrey800
            val SuspendedOrExpelled = MaterialIndigo600
        }
    }

    object Graph {

        val Primary = arrayOf(
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

        val Secondary = arrayOf(
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
