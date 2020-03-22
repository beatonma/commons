package org.beatonma.commons.data.core

import android.content.Context
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.Result
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.entities.FeaturedMember
import org.beatonma.commons.data.core.room.entities.FeaturedMemberProfile
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonsRepository @Inject constructor(
    val context: Context,
    private val commonsRemoteDataSource: CommonsRemoteDataSource,
    commonsDatabase: CommonsDatabase
) {
    private val memberDao = commonsDatabase.memberDao()

    fun observeFeaturedPeople(): LiveData<Result<List<FeaturedMemberProfile>>> = resultLiveData(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { commonsRemoteDataSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            memberDao.apply {
                profiles.forEach {
                    insertParty(it.party)
                    insertConstituency(it.constituency)
                }

                insertProfiles(profiles)
                insertFeaturedPeople(
                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
                )
            }
        }
    )
}
