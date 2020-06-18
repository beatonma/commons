package org.beatonma.commons.app.search

import android.Manifest
import androidx.appcompat.widget.SearchView
import org.beatonma.commons.data.core.search.SearchResult
import org.beatonma.commons.kotlin.extensions.PermissionResults
import org.beatonma.lib.ui.recyclerview.TypedRecyclerViewAdapter

private const val TAG = "SearchHost"


interface SearchHost {
    companion object {
        const val REQUEST_CODE_LOCATION = 4571
    }

    val searchViewModel: SearchViewModel
    val searchAdapter: TypedRecyclerViewAdapter<SearchResult>
    var searchView: SearchView

    fun findMemberForCurrentLocation()
    fun showPermissionDenied(permission: String)
    fun isSearchUiVisible(): Boolean
    fun showSearch()
    fun hideSearch()
    fun onSearchViewClosed()
    fun openSearchResult(searchResult: SearchResult)

    fun setupSearchView(searchView: SearchView) = searchView.apply {
        setIconifiedByDefault(true)
        setOnSearchClickListener { showSearch() }
        setOnQueryTextListener(continuousQueryListener { executeSearch(it) })
        setOnCloseListener { onSearchViewClosed(); false }
    }

    fun executeSearch(query: String?) {
        if (query.isNullOrBlank()) {
            searchAdapter.items = null
        } else {
            searchViewModel.submitSearch(query)
        }
    }

    fun onRequestSearchPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResult: IntArray
    ): Boolean {
        println("onRequestSearchPermissisonsResult")
        val results = PermissionResults(permissions, grantResult)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                results.check(Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
                    if (granted) findMemberForCurrentLocation()
                    else showPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
        return false
    }

    private inline fun continuousQueryListener(crossinline block: (String?) -> Unit) = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            block(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            block(newText)
            return true
        }
    }
}
