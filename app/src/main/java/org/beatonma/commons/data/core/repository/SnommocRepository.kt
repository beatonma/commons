package org.beatonma.commons.data.core.repository

import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.LiveDataIoResultList
import org.beatonma.commons.data.core.MessageOfTheDay
import org.beatonma.commons.data.resultLiveDataNoCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnommocRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
) {
    fun observeMotd(): LiveDataIoResultList<MessageOfTheDay> =
        resultLiveDataNoCache { remoteSource.getMessageOfTheDay() }
}
