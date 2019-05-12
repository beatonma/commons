@file:JvmName("Converters")

package org.beatonma.commons.data.twfy

import androidx.room.TypeConverter
import org.beatonma.commons.political.organisation.Party
import org.beatonma.commons.political.organisation.getParty

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromParty(party: Party?) = party?.id ?: ""

        @TypeConverter
        @JvmStatic
        fun toParty(id: String) = getParty(id)
    }
}
