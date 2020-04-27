package org.beatonma.commons.data.core

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.VoteType
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.Post

object Converters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    @JvmStatic
    fun serializeParty(obj: Party): Int = obj.parliamentdotuk  // Convert to foreign key

    @TypeConverter
    @JvmStatic
    fun deserializeParty(key: Int): Party? = Party(parliamentdotuk = key, name = "")

    @TypeConverter
    @JvmStatic
    fun serializeConstituency(obj: Constituency?): Int? =
        obj?.parliamentdotuk  // Convert to foreign key

    @TypeConverter
    @JvmStatic
    fun deserializeConstituency(key: Int?): Constituency? = key?.let {
        Constituency(parliamentdotuk = key, name = "")
    }

    @TypeConverter
    @JvmStatic
    fun serializeEnum(type: Enum<*>): Int = type.ordinal

    @TypeConverter
    @JvmStatic
    fun deserializePostType(ordinal: Int): Post.PostType? = ordinal.toEnum<Post.PostType>()

    @TypeConverter
    @JvmStatic
    fun deserializeHouse(ordinal: Int): House? = ordinal.toEnum<House>()

    @TypeConverter
    @JvmStatic
    fun deserializeDivisionVoteType(ordinal: Int): VoteType? = ordinal.toEnum<VoteType>()

    private inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

    private inline fun <reified T> T.toJson(): String = moshi.adapter(T::class.java).toJson(this)

    private fun <T> String.fromJson(
        cls: Class<*>,
    ): T? = moshi.adapter<T>(cls).fromJson(this)
}
