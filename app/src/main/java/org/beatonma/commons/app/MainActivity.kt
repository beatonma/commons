package org.beatonma.commons.app

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.search.SearchHost
import org.beatonma.commons.app.search.SearchViewModel
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.app.ui.recyclerview.viewholder.staticViewHolderOf
import org.beatonma.commons.commonsApp
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.data.core.search.SearchResult
import org.beatonma.commons.databinding.ActivityNavhostMainBinding
import org.beatonma.commons.databinding.ItemSearchResultBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.inflate
import org.beatonma.commons.kotlin.extensions.toast
import org.beatonma.commons.kotlin.extensions.withPermission
import org.beatonma.commons.network.retrofit.CommonsService

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : DayNightActivity(), SearchHost {

    override val searchViewModel: SearchViewModel by viewModels()

    private lateinit var binding: ActivityNavhostMainBinding
    override lateinit var searchView: SearchView

    override val searchAdapter = SearchResultsAdapter()
    lateinit var navController: NavController

    private val updateSearchEnabled = {
        lifecycleScope.launch(Dispatchers.Main) {
            // Arbitrary delay to avoid clashes with fragment transitions/child layout animations
            // on fragment change
            // TODO currently disabled while making other transitions
//            delay(300)
//            when (getContentFragment()) {
//                is SearchEnabled -> enableSearch()
//                else -> disableSearch()
//            }
        }
        Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavhostMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        initUi()

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView(searchView)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        val consumedBySearch = onRequestSearchPermissionsResult(
            requestCode, permissions, grantResults
        )
        if (consumedBySearch) return

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        val fragment = getContentFragment()

        if (fragment is BackPressConsumer) {
            if (fragment.onBackPressed()) {
                // Back press consumed by fragment
                return
            }
        }

        if (isSearchUiVisible()) {
            hideSearch()
            return
        }
        super.onBackPressed()
    }

    private fun initUi() {
        binding.searchResultsRecyclerview.setup(searchAdapter)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.overlay.setOnClickListener { hideSearch() }

        searchViewModel.resultLiveData.observe(this@MainActivity) { results ->
            searchAdapter.items = results
        }

        // Update search UI visibility - we may have entered via deep-link to arbitrary destination.
        updateSearchEnabled.invoke()
    }

    /**
     * FragmentManager instance that is responsible for navController content
     */
    private fun getNavigationFragmentManager() =
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager

    /**
     * Return the fragment that is currently the navController destination.
     */
    private fun getContentFragment(): Fragment? =
        getNavigationFragmentManager()?.primaryNavigationFragment  // Foreground fragment

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEARCH -> intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                executeSearch(query)
            }
        }
    }

    override fun findMemberForCurrentLocation() {
        withPermission(Manifest.permission.ACCESS_COARSE_LOCATION, SearchHost.REQUEST_CODE_LOCATION) {
            searchViewModel.findMemberForLocalPostcode().observe(this) { memberSearchResult ->
                openSearchResult(memberSearchResult)
                CommonsService.getMemberUrl(memberSearchResult.parliamentdotuk).toUri()
                toast("LOCAL MEMBER: BACKEND NOT IMPLEMENTED")
                // TODO backend not implemented
            }
        }
    }

    override fun showPermissionDenied(permission: String) {
        toast("Permission denied: $permission")
        // TODO
    }

    override fun enableSearch() {
        transitionToState(R.id.hide_search_results)
    }

    override fun disableSearch() {
        transitionToState(R.id.disable_search)
    }

    override fun isSearchUiVisible(): Boolean {
        return binding.mainActivityMotion.currentState == R.id.show_search_results
    }

    override fun showSearch() {
        transitionToState(R.id.show_search_results)
    }

    override fun hideSearch() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        }
    }

    override fun onSearchViewClosed() {
        transitionToState(R.id.hide_search_results)
    }

    override fun openSearchResult(searchResult: SearchResult) {
        searchView.onActionViewCollapsed()
        transitionToState(R.id.hide_search_results)
        navController.navigate(searchResult.toUri())
    }

    override fun onPause() {
        super.onPause()
        getNavigationFragmentManager()?.removeOnBackStackChangedListener(updateSearchEnabled)
    }

    override fun onResume() {
        super.onResume()
        getNavigationFragmentManager()?.addOnBackStackChangedListener(updateSearchEnabled)
    }

    override fun onDestroy() {
        commonsApp.scheduleDatabaseCleanup()
        super.onDestroy()
    }

    private fun transitionToState(@IdRes stateId: Int) {
        binding.mainActivityMotion.transitionToState(stateId)
    }

    inner class SearchResultsAdapter : LoadingAdapter<SearchResult>(
        nullLayoutID = R.layout.item_search_lookup_local_member,
        emptyLayoutID = R.layout.item_search_result_empty
    ) {

        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            object : TypedViewHolder(parent.inflate(R.layout.item_search_result)) {
                private val vh = ItemSearchResultBinding.bind(itemView)
                override fun bind(item: SearchResult) {
                    when (item) {
                        is MemberSearchResult -> {
                            vh.apply {
                                bindText(
                                    name to item.name,
                                    detail to (item.currentPost ?: item.constituency?.name ?: item.party?.name),
                                )
                            }

                            itemView.setOnClickListener {
                                openSearchResult(item)
                            }
                        }
                    }
                }
            }

        override fun getNullViewHolder(view: View): RecyclerView.ViewHolder =
            staticViewHolderOf(view) {
                itemView.setOnClickListener {
                    findMemberForCurrentLocation()
                }
            }

        override fun getEmptyViewHolder(view: View): RecyclerView.ViewHolder = staticViewHolderOf(view)
    }
}
