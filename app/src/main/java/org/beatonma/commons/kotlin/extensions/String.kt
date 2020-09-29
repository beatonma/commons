package org.beatonma.commons.kotlin.extensions

import android.content.Context
import org.beatonma.commons.R


/**
 * Join the given string components with a dot between each pair
 */
fun Context.dotted(vararg components: String?): String =
    components.filterNotNull()
        .joinToString(
            separator = " ${stringCompat(R.string.dot)} "
        )

fun Context.dotted(components: List<String?>): String =
    components.filterNotNull()
        .joinToString (
            separator = " ${stringCompat(R.string.dot)} "
        )
