package org.beatonma.commons.app.search

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.data.CommonsRemoteDataSourceImpl
import org.beatonma.commons.data.SuccessResult
import org.beatonma.commons.data.core.search.MemberSearchResult
import javax.inject.Inject

private const val TAG = "SearchViewModel"

class SearchViewModel @Inject constructor(
    application: CommonsApplication,
    private val dataSource: CommonsRemoteDataSourceImpl
) : AndroidViewModel(application) {

    private var networkJob: Job? = null
    val resultLiveData = MutableLiveData<List<MemberSearchResult>>()

    fun submitSearch(query: String) {
        networkJob?.cancel()
        networkJob = viewModelScope.launch(Dispatchers.IO) {
            val result = dataSource.getSearchResults(query)
            Log.i(TAG, "SEARCH RESULT: ${result.message}")
            if (result is SuccessResult) {
                resultLiveData.postValue(result.data)
            }
        }
    }
}
