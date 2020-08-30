package org.beatonma.commons.app.search

import android.location.Geocoder
import android.util.Log
import androidx.core.location.component1
import androidx.core.location.component2
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.SuccessResult
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.data.core.search.SearchResult

private const val TAG = "SearchViewModel"

class SearchViewModel @ViewModelInject constructor(
    private val dataSource: CommonsRemoteDataSource,
    private val locationProvider: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) : ViewModel() {

    private var networkJob: Job? = null
    val resultLiveData = MutableLiveData<List<SearchResult>>()
    val localMemberLiveData = MutableLiveData<MemberSearchResult>()

    fun submit(query: String) {
        networkJob?.cancel()
        networkJob = viewModelScope.launch(Dispatchers.IO) {
            val result = dataSource.getSearchResults(query)
            if (result is SuccessResult) {
                resultLiveData.postValue(result.data ?: listOf())
            }
        }
    }

    fun findMemberForLocalPostcode(): LiveData<MemberSearchResult> {
        networkJob?.cancel()
        networkJob = viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "Lookup up postcode...")
            val postcode = getLocalPostcode()
            localMemberLiveData.postValue(
                MemberSearchResult(
                    parliamentdotuk = 0,
                    name = postcode ?: "NO POSTCODE",
                    portraitUrl = null,
                    party = null,
                    constituency = null,
                    currentPost = null,
                ))
            TODO("We've got the postcode but we still need to use it to look up the constituency/member!")
        }
        return localMemberLiveData
    }

    private suspend fun getLocalPostcode(): String? = withContext(Dispatchers.IO) {
        try {
            val location = locationProvider.lastLocation.await()
            val (latitude, longitude) = location
            geocoder.getFromLocation(latitude, longitude, 1)
                .firstOrNull()
                ?.postalCode
                ?: null.also { Log.w(TAG, "Unable to find postcode for location") }
        }
        catch (e: SecurityException) {
            Log.w(TAG, "Location requested without permission.")
            null
        }
    }
}
