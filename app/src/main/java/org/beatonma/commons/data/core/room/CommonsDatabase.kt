package org.beatonma.commons.data.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.beatonma.commons.data.core.Converters
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.*


const val COMMONS_DB_FILENAME = "commons.db"

@Database(
    entities = [
        MemberProfile::class,
        Birthplace::class,
        Constituency::class,
        Party::class,
        FeaturedMember::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CommonsDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
}
