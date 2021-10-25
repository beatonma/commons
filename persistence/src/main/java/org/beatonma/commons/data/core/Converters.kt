package org.beatonma.commons.data.core

import androidx.room.TypeConverter
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.Post
import java.time.LocalDate

object Converters {

    @TypeConverter
    @JvmStatic
    fun serializeDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    @JvmStatic
    fun deserializeDate(time: Long?): LocalDate? = if (time == null) null else LocalDate.ofEpochDay(time)

    @TypeConverter
    @JvmStatic
    fun serializeParty(obj: Party): Int = obj.parliamentdotuk

    @TypeConverter
    @JvmStatic
    fun deserializeParty(key: Int): Party = Party(parliamentdotuk = key, name = "")

    @TypeConverter
    @JvmStatic
    fun serializeConstituency(obj: Constituency?): Int? = obj?.parliamentdotuk

    @TypeConverter
    @JvmStatic
    fun deserializeConstituency(key: Int?): Constituency? = key?.let {
        Constituency(parliamentdotuk = key, name = "")
    }

    @TypeConverter
    @JvmStatic
    fun serializeMemberProfile(obj: MemberProfile?): Int? = obj?.parliamentdotuk

    @TypeConverter
    @JvmStatic
    fun deserializeMemberProfile(key: Int?): MemberProfile? = key?.let {
        MemberProfile(
            parliamentdotuk = key,
            name = "",
            constituency = null,
            party = NoParty,
        )
    }

    @TypeConverter
    @JvmStatic
    fun serializeEnum(type: Enum<*>): Int = type.ordinal

    @TypeConverter
    @JvmStatic
    fun deserializePostType(ordinal: Int): Post.PostType = ordinal.toEnum()

    @TypeConverter
    @JvmStatic
    fun deserializeHouse(ordinal: Int): House = ordinal.toEnum()

    @TypeConverter
    @JvmStatic
    fun deserializeDivisionVoteType(ordinal: Int): VoteType = ordinal.toEnum()

    private inline fun <reified E : Enum<E>> Int.toEnum(): E = enumValues<E>()[this]
}
