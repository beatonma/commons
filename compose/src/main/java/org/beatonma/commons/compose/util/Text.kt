package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable

private const val DOT = '·'

@Composable
fun dotted(vararg components: String?) =
    components.filterNot(String?::isNullOrEmpty)
        .joinToString(
            separator = " $DOT "
        )
