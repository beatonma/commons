package org.beatonma.commons.app.constituency

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.getTheme
import org.beatonma.commons.app.ui.recyclerview.CommonsLoadingAdapter
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.databinding.FragmentConstituencyDetailBinding
import org.beatonma.commons.databinding.H2Binding
import org.beatonma.commons.databinding.ItemWideImageTitleSubtitleDescriptionBinding
import org.beatonma.commons.databinding.ItemWideTitleDescriptionBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setup

private const val TAG = "ConstitDetailFragment"
private const val MAPVIEW_BUNDLE_KEY = "MapViewBundle"
private const val CAMERA_PADDING_DP = 64
private const val VIEW_TYPE_FIRST = 1
private const val VIEW_TYPE_HEADER = 345

class ConstituencyDetailFragment : BaseViewmodelFragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var binding: FragmentConstituencyDetailBinding
    private val viewmodel: ConstituencyDetailViewModel by viewModels { viewmodelFactory }
    private var constituencyId: ParliamentID = 0

    private val resultsAdapter = ElectionResultsAdapter()

    private val mapView: MapView? get() = binding.mapview
    private var gMap: GoogleMap? = null

    private fun getConstituencyFromBundle(): ParliamentID? =
        arguments?.getInt(PARLIAMENTDOTUK)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parliamentdotuk = getConstituencyFromBundle()
        if (parliamentdotuk == null) {
            Log.w(TAG, "Failed to get constituency ID from bundle!")
            return
        }
        constituencyId = parliamentdotuk
        viewmodel.forConstituency(parliamentdotuk)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentConstituencyDetailBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapview.viewTreeObserver.addOnGlobalLayoutListener(this)

        binding.electionResultsRecyclerview.setup(resultsAdapter)

        viewmodel.liveData.observe(viewLifecycleOwner) {
            updateConstituencyUi(it.constituency, it.theme)
            updateGeometryUi(it.geometry, it.theme)
            resultsAdapter.items = it.electionResults
        }
    }

    override fun onGlobalLayout() {
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
        initiateMap(binding.mapview)
    }

    private fun initiateMap(mapView: MapView) {
        mapView.getMapAsync { map ->
            gMap = map

            map.moveCameraTo(viewmodel.getUkBounds())

            context?.ifPermissionAvailable(Manifest.permission.ACCESS_COARSE_LOCATION) {
                try {
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
                }
                catch (e: SecurityException) { /* Not actually required - already caught by ifPermissionAvailable but IDE complains */ }
            }
        }
    }

    private fun updateConstituencyUi(constituency: Constituency?, theme: PartyColors?) {
        constituency ?: return
        binding.apply {
            bindText(
                constituencyName to constituency.name,
            )
        }
    }

    private fun updateGeometryUi(geometry: Geometry?, theme: PartyColors?) {
        geometry ?: return
        theme ?: return
        val map = gMap ?: return
        updateMap(map, geometry, theme)
    }

    private fun updateMap(gMap: GoogleMap, geometry: Geometry, theme: PartyColors) {
        gMap.apply {
            clear()
            setMapStyle(viewmodel.getMapStyle())
            geometry.polygons.forEach { polygon -> addPolygon(polygon) }

            moveCameraTo(geometry.boundary)
        }
    }

    /*
     * All lifecycle events must be passed to the MapView
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapState = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)

        mapView?.onCreate(mapState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Get map state and inject an empty value if not already set
        val mapState = outState.getBundle(MAPVIEW_BUNDLE_KEY)
            ?: bundleOf().also { outState.putBundle(MAPVIEW_BUNDLE_KEY, it) }

        mapView?.onSaveInstanceState(mapState)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
    /*
     * Lifecycle end
     */


    private fun GoogleMap.moveCameraTo(boundary: LatLngBounds) {
        moveCamera(CameraUpdateFactory.newLatLngBounds(boundary, context.dp(CAMERA_PADDING_DP)))
    }

    private inner class ElectionResultsAdapter: CommonsLoadingAdapter<ConstituencyDataHolder>() {
        override fun getItemViewType(position: Int): Int {
            return when {
                items?.isEmpty() != false -> super.getItemViewType(position)
                else -> when(items?.get(position)) {
                    is ConstituencyFirstResultData -> VIEW_TYPE_FIRST
                    is ConstituencyHeaderData -> VIEW_TYPE_HEADER
                    else -> super.getItemViewType(position)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.h2))
            VIEW_TYPE_FIRST -> LatestElectionResultViewHolder(parent.inflate(R.layout.item_wide_image_title_subtitle_description))
            else -> super.onCreateViewHolder(parent, viewType)
        }

        override fun onCreateDefaultViewHolder(parent: ViewGroup): TypedViewHolder =
            ElectionResultViewHolder(parent.inflate(R.layout.item_wide_title_description))

        private inner class HeaderViewHolder(view: View) : TypedViewHolder(view) {
            private val vh = H2Binding.bind(view)

            override fun bind(item: ConstituencyDataHolder) {
                val title = item as? ConstituencyHeaderData ?: return
                vh.title.text = title.title
            }
        }

        private inner class LatestElectionResultViewHolder(view: View) : TypedViewHolder(view) {
            private val vh = ItemWideImageTitleSubtitleDescriptionBinding.bind(view)

            override fun bind(item: ConstituencyDataHolder) {
                val result = item as? ConstituencyFirstResultData ?: return

                val profile = result.profile
                val election = result.election
                val theme = profile.party.getTheme(context)

                vh.apply {
                    bindText(
                        title to profile.name,
                        subtitle to election.name,
                        description to profile.currentPost,
                        linkColor = theme.accent,
                    )

                    portrait.load(profile.portraitUrl)
                    theme.also { accent.setBackgroundColor(it.primary) }
                }

                itemView.setOnClickListener { view ->
                    view.navigateToElectionDetails(constituencyId, election.parliamentdotuk)
                }
            }
        }

        private inner class ElectionResultViewHolder(view: View) : TypedViewHolder(view) {
            private val vh = ItemWideTitleDescriptionBinding.bind(view)

            override fun bind(item: ConstituencyDataHolder) {
                val result = item as? ConstituencyResultData ?:return

                val profile = result.profile
                val election = result.election
                val theme = profile.party.getTheme(context)

                vh.apply {
                    bindText(
                        title to profile.name,
                        description to election.name,
                        linkColor = theme.accent,
                    )

                    theme.also { accent.setBackgroundColor(it.primary) }
                }

                itemView.setOnClickListener { view ->
                    view.navigateToElectionDetails(constituencyId, election.parliamentdotuk)
                }
            }
        }
    }
}


private fun View.navigateToElectionDetails(constituencyId: ParliamentID, electionId: ParliamentID) {
    navigateTo(
        R.id.action_constituencyDetailFragment_to_constituencyElectionResultsFragment,
        bundleOf(
            CONSTITUENCY_ID to constituencyId,
            ELECTION_ID to electionId,
        )
    )
}
