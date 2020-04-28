package org.beatonma.commons.app.ui

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.beatonma.commons.data.IoResult

abstract class SnippetGeneratorAndroidViewModel<D>(application: Application): BaseIoAndroidViewModel<D>(application) {
    val snippetsLiveData: MutableLiveData<List<Snippet>> = MutableLiveData()

    private var snippetJob: Job? = null
    private val snippetGeneratorObserver = Observer<IoResult<D>> {
        it.data?.let { data ->
            snippetJob?.cancel()
            snippetJob = viewModelScope.launch {
                snippetsLiveData.value = generateSnippets(data)
            }
        }
    }

    abstract suspend fun generateSnippets(data: D): List<Snippet>

    /**
     * Must be called once liveData has been initiated!
     */
    @CallSuper
    open fun observe() {
        liveData.observeForever(snippetGeneratorObserver)
    }

    @CallSuper
    override fun onCleared() {
        snippetJob?.cancel()
        liveData.removeObserver(snippetGeneratorObserver)
        super.onCleared()
    }
}


/**
 * Generate an array of Snippets from a given list of objects
 */
fun <T> List<T>?.toSnippets(builder: ((T) -> Snippet?)): Array<Snippet> =
    this?.mapNotNull { builder(it) }?.toTypedArray() ?: arrayOf()

/**
 * Container for surfacing small chunks of information to display.
 */
data class Snippet(
    val title: String,
    val content: String,
    val subtitle: String? = null,
    val subcontent: String? = null,
    val clickActionText: String? = null, // If onclick is set this text will be displayed on the button
    val onclick: ((Context) -> Unit)? = null,
)
