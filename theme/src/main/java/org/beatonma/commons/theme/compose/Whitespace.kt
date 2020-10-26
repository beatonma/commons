package org.beatonma.commons.theme.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

object Whitespace {
    object List {
        object Vertical {
            val between = 8.dp
            val around = 16.dp
        }

        object Horizontal {
            val between = 12.dp
            val around = 16.dp
        }
    }

    object Image {
        val around = 8.dp
    }

    object Icon {
        object Small {
            val around = 8.dp
        }

        object Large {
            val around = 16.dp
        }
    }

    object TextList {
        val between = 8.dp
    }

    object WindowContent {
        val bottom = 160.dp
    }
}

object Padding {
    val VerticalList = PaddingValues(bottom = Whitespace.List.Vertical.between)
    val Card = PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp)
}
