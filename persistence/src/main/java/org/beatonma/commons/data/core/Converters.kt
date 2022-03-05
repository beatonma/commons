package org.beatonma.commons.data.core

import androidx.room.TypeConverter
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.NoConstituency
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.Post
import java.time.LocalDate
import java.time.LocalDateTime

object Converters {
    @TypeConverter
    @JvmStatic
    fun serializeDate(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    @JvmStatic
    fun deserializeDate(time: Long?): LocalDate? = if (time == null) null else LocalDate.ofEpochDay(time)

    @TypeConverter
    @JvmStatic
    fun serializeParty(obj: Party?): Int? = obj?.parliamentdotuk

    @TypeConverter
    @JvmStatic
    fun deserializeParty(key: Int?): Party? = key?.let { NoParty.copy(parliamentdotuk = key) }

    @TypeConverter
    @JvmStatic
    fun serializeConstituency(obj: Constituency?): Int? = obj?.parliamentdotuk

    @TypeConverter
    @JvmStatic
    fun deserializeConstituency(key: Int?): Constituency? = key?.let {
        NoConstituency.copy(parliamentdotuk = key)
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
    fun serializeEnum(type: Enum<*>): String = type.name

    @TypeConverter
    @JvmStatic
    fun deserializePostType(name: String): Post.PostType = name.toEnum()

    @TypeConverter
    @JvmStatic
    fun deserializeHouse(name: String): House = name.toEnum()

    @TypeConverter
    @JvmStatic
    fun deserializeDivisionVoteType(name: String): VoteType = name.toEnum()

    @TypeConverter
    @JvmStatic
    fun serializeDates(dates: List<LocalDate>) = dates.joinToString(",")

    @TypeConverter
    @JvmStatic
    fun deserializeDates(csv: String): List<LocalDate> =
        csv.split(",").map(LocalDate::parse)

    @TypeConverter
    @JvmStatic
    fun serializeDateTime(dt: LocalDateTime?): String? = dt?.toString()

    @TypeConverter
    @JvmStatic
    fun deserializeDateTime(value: String?): LocalDateTime? = value?.let(LocalDateTime::parse)

    @TypeConverter
    @JvmStatic
    fun serializeDateTimes(datetimes: List<LocalDateTime>) = datetimes.joinToString(",")

    @TypeConverter
    @JvmStatic
    fun deserializeDateTimes(csv: String): List<LocalDateTime> =
        csv.split(",").map(LocalDateTime::parse)

    private inline fun <reified E : Enum<E>> String.toEnum(): E = enumValueOf(this)
}
