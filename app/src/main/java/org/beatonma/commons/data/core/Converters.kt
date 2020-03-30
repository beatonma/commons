package org.beatonma.commons.data.core

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import org.beatonma.commons.data.core.room.entities.*
import java.util.*

object Converters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter @JvmStatic
    fun serializeParty(obj: Party): Int = obj.parliamentdotuk  // Convert to foreign key

    @TypeConverter @JvmStatic
    fun deserializeParty(key: Int): Party? = Party(parliamentdotuk = key, name = "")

    @TypeConverter @JvmStatic
    fun serializeConstituency(obj: Constituency?): Int? = obj?.parliamentdotuk  // Convert to foreign key

    @TypeConverter @JvmStatic
    fun deserializeConstituency(key: Int?): Constituency? = key?.let{ Constituency(parliamentdotuk = key, name = "") }

    @TypeConverter @JvmStatic
    fun serializeTown(town: Town?): String = town.toJson()

    @TypeConverter @JvmStatic
    fun deserializeTown(json: String): Town? = json.fromJson(Town::class.java)

    @TypeConverter @JvmStatic
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter @JvmStatic
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter @JvmStatic
    fun serializeEnum(type: Enum<*>): Int = type.ordinal

    @TypeConverter @JvmStatic
    fun deserializePostType(ordinal: Int): Post.PostType? = ordinal.toEnum<Post.PostType>()

    @TypeConverter @JvmStatic
    fun deserializeHouse(ordinal: Int): HouseMembership.House? = ordinal.toEnum<HouseMembership.House>()


    private inline fun <reified T: Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

    private inline fun <reified T> T.toJson(): String = moshi.adapter(T::class.java).toJson(this)

    private fun <T> String.fromJson(
        cls: Class<*>
    ): T? = moshi.adapter<T>(cls).fromJson(this)
}
