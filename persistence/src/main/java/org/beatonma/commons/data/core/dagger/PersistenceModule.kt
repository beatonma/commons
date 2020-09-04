package org.beatonma.commons.data.core.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.data.core.room.COMMONS_DB_FILENAME
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.dao.*
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class)
class PersistenceModule {
    @Singleton @Provides
    fun provideCommonsDatabase(@ApplicationContext context: Context): CommonsDatabase = Room.databaseBuilder(
        context.applicationContext,
        CommonsDatabase::class.java,
        COMMONS_DB_FILENAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton @Provides
    fun providesPeopleDao(commonsDatabase: CommonsDatabase): MemberDao = commonsDatabase.memberDao()

    @Singleton @Provides
    fun providesBillDao(commonsDatabase: CommonsDatabase): BillDao = commonsDatabase.billDao()

    @Singleton @Provides
    fun provideDivisionDao(commonsDatabase: CommonsDatabase): DivisionDao =
        commonsDatabase.divisionDao()

    @Singleton @Provides
    fun provideConstituencyDao(commonsDatabase: CommonsDatabase): ConstituencyDao =
        commonsDatabase.constituencyDao()

    @Singleton @Provides
    fun providesUserDao(commonsDatabase: CommonsDatabase): UserDao = commonsDatabase.userDao()

    // Database cleanup DAOs
    @Singleton @Provides
    fun providesMemberCleanupDao(commonsDatabase: CommonsDatabase): MemberCleanupDao = commonsDatabase.memberCleanupDao()
}
