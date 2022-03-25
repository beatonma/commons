package org.beatonma.commons.app.ui.screens.search

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.repo.repository.SearchRepository
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val locationProvider: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val savedStateHandle: SavedStateHandle,
) : IoLiveDataViewModel<List<SearchResult>>() {
    fun submit(query: String) {
        viewModelScope.launch {
            repository.getSearchResults(query)
                .collectLatest(this@SearchViewModel::postValue)
        }
    }

    fun findMemberForLocalPostcode(): LiveData<MemberSearchResult> {
        TODO()
    }

    private suspend fun getLocalPostcode(): String? = withContext(Dispatchers.IO) {
        TODO()
    }
}
