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
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.search.SearchEnabled
import org.beatonma.commons.app.search.SearchHost
import org.beatonma.commons.app.search.SearchViewModel
import org.beatonma.commons.app.signin.SignInEnabled
import org.beatonma.commons.app.signin.SignInHost
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.recyclerview.adapter.AsyncDiffHost
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.app.ui.recyclerview.viewholder.staticViewHolderOf
import org.beatonma.commons.commonsApp
import org.beatonma.commons.databinding.ActivityNavhostMainBinding
import org.beatonma.commons.databinding.ItemSearchResultBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult

private const val TAG = "MainActivity"

private typealias Flag = Int
private object State {
    const val SEARCH_ENABLED: Flag = 0b1
    const val SEARCH_EXPANDED: Flag = 0b10
    const val SIGNIN_ENABLED: Flag = 0b100
}

@AndroidEntryPoint
@Deprecated("Replaced by MainComposeActivity")
class MainActivity : DayNightActivity(), SearchHost, SignInHost, AsyncDiffHost {

    override val searchViewModel: SearchViewModel by viewModels()

    private lateinit var binding: ActivityNavhostMainBinding
    override lateinit var searchView: SearchView

    override var diffJob: Job? = null

    override val searchAdapter = SearchResultsAdapter()
    lateinit var navController: NavController

    private var uiStateFlags = State.SEARCH_ENABLED or State.SIGNIN_ENABLED
        set(value) {
            field = value
            transitionToState(getMotionState(value))
        }


    private val updateSearchEnabled = {
        lifecycleScope.launch(Dispatchers.Main) {
            // Semi-arbitrary delay to avoid clashes with fragment transitions/child layout animations
            // on fragment change
            delay(longCompat(R.integer.animation_window_duration))
            val contentFragment = getContentFragment()
            setUiFeatures(
                searchEnabled = contentFragment is SearchEnabled,
                signinEnabled = contentFragment is SignInEnabled,
            )
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
        if (isSearchUiVisible()) {
            hideSearch()
            return
        }

        val fragment = getContentFragment()

        if (fragment is BackPressConsumer && fragment.onBackPressed()) {
            // Back press consumed by fragment
            return
        }
        super.onBackPressed()
    }

    private fun initUi() {
        binding.root.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        binding.root.registerInsets(
            fromStatusBar = listOf(binding.toolbar),
            fromNavBar = listOf(binding.signinButton),
        )

        binding.searchResultsRecyclerview.setup(searchAdapter)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.overlay.setOnClickListener { hideSearch() }

        searchViewModel.resultLiveData.observe(this@MainActivity) { results ->
            searchAdapter.diffItems(results)
        }

        // Update search UI visibility - we may have entered via deep-link to arbitrary destination.
        updateSearchEnabled.invoke()

        binding.signinButton.setOnClickListener {
            showSignInDialog(this)
        }

        ViewCompat.requestApplyInsets(binding.signinButton)
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

    /**
     * Compose new UI state with the specified features as a single value change to avoid
     * making multiple calls to [transitionToState].
     */
    private fun setUiFeatures(searchEnabled: Boolean, signinEnabled: Boolean) {
        var newState = uiStateFlags
        newState = newState.setFlag(State.SEARCH_ENABLED, searchEnabled)
        newState = newState.setFlag(State.SIGNIN_ENABLED, signinEnabled)

        uiStateFlags = newState
    }

    override fun enableSignIn() {
        uiStateFlags = uiStateFlags.addFlag(State.SIGNIN_ENABLED)
    }

    override fun disableSignIn() {
        uiStateFlags = uiStateFlags.removeFlag(State.SIGNIN_ENABLED)
    }

    override fun enableSearch() {
        uiStateFlags = uiStateFlags.addFlag(State.SEARCH_ENABLED)
    }

    override fun disableSearch() {
        uiStateFlags = uiStateFlags.removeFlag(State.SEARCH_ENABLED)
    }

    override fun isSearchUiVisible(): Boolean {
        return uiStateFlags.hasFlag(State.SEARCH_EXPANDED)
    }

    override fun showSearch() {
        uiStateFlags = uiStateFlags.addFlag(State.SEARCH_EXPANDED)
    }

    override fun hideSearch() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        }
    }

    override fun onSearchViewClosed() {
        uiStateFlags = uiStateFlags.removeFlag(State.SEARCH_EXPANDED)
    }

    override fun openSearchResult(searchResult: SearchResult) {
        searchView.onActionViewCollapsed()
        uiStateFlags = uiStateFlags.removeFlag(State.SEARCH_EXPANDED)
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

    /**
     * Given some combination of State flags, return the corresponding MotionLayout state ID.
     */
    @IdRes
    private fun getMotionState(state: Int): Int {
        return when {
            state.hasFlag(State.SEARCH_EXPANDED) -> R.id.state_search_show_results
            state.hasAllFlags(State.SEARCH_ENABLED, State.SIGNIN_ENABLED) -> R.id.state_enable_search_signin
            state.hasFlag(State.SEARCH_ENABLED) -> R.id.state_enable_search
            state.hasFlag(State.SIGNIN_ENABLED) -> R.id.state_enable_signin
            else -> R.id.state_disable_search_signin
        }
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
