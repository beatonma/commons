package org.beatonma.commons.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.beatonma.commons.data.core.*
import org.beatonma.commons.data.wikipedia.*


const val COMMONS_DB_FILENAME = "commons.db"

@Database(
    entities = [
        ProfileImage::class,
        WikiSummary::class,
        Lookup::class,
        Profile::class,
        ContactInfo::class,
        Career::class,
        Interests::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class CommonsDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun peopleDao(): PeopleDao
    abstract fun summaryDao(): WikiSummaryDao
    abstract fun lookupDao(): LookupDao
}
