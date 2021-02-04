package org.beatonma.commons.app.search

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import javax.inject.Inject

private const val TAG = "SearchViewModel"
private const val QUERY = "query"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataSource: CommonsApi,
    private val locationProvider: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var networkJob: Job? = null
    private val _resultLiveData = MutableLiveData<List<SearchResult>>()
    val resultLiveData: LiveData<List<SearchResult>> get() = _resultLiveData

//    val localMemberLiveData = MutableLiveData<MemberSearchResult>()

    fun submit(query: String) {
        savedStateHandle[QUERY] = query
        networkJob?.cancel()
        networkJob = viewModelScope.launch(Dispatchers.IO) {
            dataSource.getSearchResults(query).onSuccess { data ->
                _resultLiveData.postValue(data)
            }
        }
    }

    fun findMemberForLocalPostcode(): LiveData<MemberSearchResult> {
        TODO()
//        networkJob?.cancel()
//        networkJob = viewModelScope.launch(Dispatchers.IO) {
//            Log.i(TAG, "Lookup up postcode...")
//            val postcode = getLocalPostcode()
//            localMemberLiveData.postValue(
//                MemberSearchResult(
//                    parliamentdotuk = 0,
//                    name = postcode ?: "NO POSTCODE",
//                    portraitUrl = null,
//                    party = null,
//                    constituency = null,
//                    currentPost = null,
//                ))
//            TODO("We've got the postcode but we still need to use it to look up the constituency/member!")
//        }
//        return localMemberLiveData
    }

    private suspend fun getLocalPostcode(): String? = withContext(Dispatchers.IO) {
        TODO()
//        try {
//            val location = locationProvider.lastLocation.await()
//            val (latitude, longitude) = location
//            geocoder.getFromLocation(latitude, longitude, 1)
//                .firstOrNull()
//                ?.postalCode
//                ?: null.also { Log.w(TAG, "Unable to find postcode for location") }
//        }
//        catch (e: SecurityException) {
//            Log.w(TAG, "Location requested without permission.")
//            null
//        }
    }


}
