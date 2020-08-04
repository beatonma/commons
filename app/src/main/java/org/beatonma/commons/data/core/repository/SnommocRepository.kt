package org.beatonma.commons.data.core.repository

import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.resultFlowNoCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnommocRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
) {
    fun getMotd() = resultFlowNoCache { remoteSource.getMessageOfTheDay() }
}
