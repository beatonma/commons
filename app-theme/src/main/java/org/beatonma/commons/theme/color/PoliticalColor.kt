package org.beatonma.commons.theme.color

import androidx.compose.ui.graphics.Color

object PoliticalColor {
    val Parliament = Color(0xff_37_31_51)  // Purple
    val Westminster = Color(0xff_e8_e9_e8) // Off-white
    val Royal = Color(0xff_23_35_80)       // Blue

    object House {
        val Commons = Color(0xff_00_6e_46)
        val CommonsDark = Color(0xff_00_42_29)
        val CommonsDeep = Color(0xff_00_54_34)

        val Lords = Color(0xff_B5_09_38)

        val Royal = PoliticalColor.Royal
        val Parliament = PoliticalColor.Parliament
    }

    object Party {
        object Primary {
            val Default = House.Parliament
            val Alliance = Color(0xff_F4_C7_2E)
            val ChangeUk = Color.White
            val Conservative = Color(0xff_11_43_7D)
            val Dup = Color(0xff_34_30_67)
            val Green = Color(0xff_53_8C_6B)
            val Independent = Default
            val Labour = Color(0xff_C4_12_30)
            val LabourCoop = Color(0xff_71_1F_8E)
            val LibDem = Color(0xff_FD_BB_30)
            val PlaidCymru = Color(0xff_00_81_42)
            val Sdlp = Color(0xff_0B_69_4D)
            val SinnFein = Color(0xff_08_67_23)
            val Snp = Color(0xff_FF_F9_5D)
            val Ukip = Color(0xff_70_2F_85)
            val Uup = Color(0xff_1D_2F_5D)
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
        val Aye = CommonsColor.Positive
        val No = CommonsColor.Negative
        val Abstain = MaterialAmber600
        val DidNotVote = MaterialGrey800
        val SuspendedOrExpelled = MaterialIndigo600
    }
}
