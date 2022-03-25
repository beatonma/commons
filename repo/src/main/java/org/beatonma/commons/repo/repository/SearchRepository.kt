package org.beatonma.commons.repo.repository

import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.repo.result.resultFlowNoCache
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val remoteSource: CommonsApi,
) {
    fun getSearchResults(query: String): ResultFlow<List<MemberSearchResult>> = resultFlowNoCache {
        when {
            query.isBlank() -> Success(listOf())
            else -> remoteSource.getSearchResults(query)
        }
    }
}
