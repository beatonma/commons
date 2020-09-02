package org.beatonma.commons.repo.repository

import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.result.resultFlowNoCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnommocRepository @Inject constructor(
    private val remoteSource: CommonsApi,
) {
    fun getMotd() = resultFlowNoCache { remoteSource.getMessageOfTheDay() }
}
