package org.beatonma.commons.data.core

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import org.beatonma.commons.data.core.room.entities.Birthplace
import org.beatonma.commons.data.core.room.entities.Constituency
import org.beatonma.commons.data.core.room.entities.Party
import java.util.*

object Converters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter @JvmStatic
    fun serializeParty(obj: Party): Int = obj.parliamentdotuk  // Convert to foreign key

    @TypeConverter @JvmStatic
    fun deserializeParty(key: Int): Party? = Party(parliamentdotuk = key, name = "")

    @TypeConverter @JvmStatic
    fun serializeConstituency(obj: Constituency): Int = obj.parliamentdotuk  // Convert to foreign key

    @TypeConverter @JvmStatic
    fun deserializeConstituency(key: Int): Constituency? = Constituency(parliamentdotuk = key, name = "")

    @TypeConverter @JvmStatic
    fun serializeBirthplace(town: Birthplace?): String = town.toJson()

    @TypeConverter @JvmStatic
    fun deserializeBirthplace(json: String): Birthplace? = json.fromJson(
        Birthplace::class.java)

    @TypeConverter @JvmStatic
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter @JvmStatic
    fun dateToTimestamp(date: Date?): Long? = date?.time

    private inline fun <reified T> T.toJson(): String = moshi.adapter(T::class.java).toJson(this)

    private fun <T> String.fromJson(
        cls: Class<*>
    ): T? = moshi.adapter<T>(cls).fromJson(this)
}
