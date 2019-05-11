package org.beatonma.commons.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.beatonma.commons.data.wikipedia.ProfileImage
import org.beatonma.commons.data.wikipedia.ImageDao

@Database(entities = [ProfileImage::class], version = 1)
abstract class CommonsDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
