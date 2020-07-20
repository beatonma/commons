package org.beatonma.commons.data.resolution

import android.content.Context
import android.util.Log
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.House

/**
 * Helper for getting more verbose descriptions of various object types
 */
fun <T: Any> Context.describe(obj: T?): String = when (obj) {
    is Named -> obj.description(this)
    is House -> obj.description(this)
    null -> ""
    else -> {
        Log.d("ctx.describe", "Unable to provide description for object of class=${obj.javaClass.canonicalName}")
        "ctx.describe"
    }
}
