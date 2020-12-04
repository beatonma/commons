package org.beatonma.commons.app.constituency

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.getTheme
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.databinding.FragmentConstituencyDetailBinding
import org.beatonma.commons.databinding.H2Binding
import org.beatonma.commons.databinding.ItemWideImageTitleSubtitleDescriptionBinding
import org.beatonma.commons.databinding.ItemWideTitleDescriptionBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.context
import org.beatonma.commons.kotlin.extensions.dimenCompat
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.kotlin.extensions.ifPermissionAvailable
import org.beatonma.commons.kotlin.extensions.inflate
import org.beatonma.commons.kotlin.extensions.load
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.kotlin.extensions.size

private const val TAG = "ConstitDetailFragment"
private const val MAPVIEW_BUNDLE_KEY = "MapViewBundle"
private const val VIEW_TYPE_FIRST = 1
private const val VIEW_TYPE_HEADER = 345

@AndroidEntryPoint
class ConstituencyDetailFragment : CommonsFragment<FragmentConstituencyDetailBinding>(), ViewTreeObserver.OnGlobalLayoutListener {
    private val viewmodel: ConstituencyDetailViewModel by viewModels()
    private var constituencyId: ParliamentID = 0

    private val resultsAdapter = ElectionResultsAdapter()

    private val mapView: MapView? get() = binding.mapview
    private var gMap: GoogleMap? = null

    private val isMapReady: Boolean get() = gMap != null && mapView?.size()?.isSet() == true

    private fun getConstituencyFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parliamentdotuk = getConstituencyFromBundle()
        constituencyId = parliamentdotuk
        viewmodel.forConstituency(parliamentdotuk)
    }

    override fun inflateBinding(inflater: LayoutInflater) = FragmentConstituencyDetailBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateMap(binding.mapview)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(this)

        binding.electionResultsRecyclerview.setup(resultsAdapter)
    }

    override fun onGlobalLayout() {
        if (binding.mapview.size().isSet()) {
            binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            if (isMapReady) {
                val map = gMap ?: throw Exception("Trying to call onMapReady with null map!")
                onMapReady(map)
            }
        }
        else {
            Log.d(TAG, "Awaiting next layout before map setup...")
        }
    }

    private fun initiateMap(mapView: MapView) {
        mapView.getMapAsync { map ->
            gMap = map
            context?.ifPermissionAvailable(Manifest.permission.ACCESS_COARSE_LOCATION) {
                try {
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
                }
                catch (e: SecurityException) { /* Not actually required - already caught by ifPermissionAvailable but IDE complains */ }
            }

            if (isMapReady) {
                onMapReady(map)
            }
        }
    }

    /**
     * Must be called after mapView.getMapAsync has returned and MapView layout has completed.
     */
    private fun onMapReady(map: GoogleMap) {
        map.apply {
            setMapStyle(viewmodel.getMapStyle())
            clear()
            moveCameraTo(viewmodel.getUkBounds())
        }

        viewmodel.liveData.observe(viewLifecycleOwner) { data ->
            updateConstituencyUi(data.constituency, data.theme)
            updateGeometryUi(data.geometry)
            resultsAdapter.diffItems(data.electionResults)
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

    private fun updateGeometryUi(geometry: Geometry?) {
        geometry ?: return
        val map = gMap ?: return
        updateMap(map, geometry)
    }

    private fun updateMap(gMap: GoogleMap, geometry: Geometry) {
        gMap.apply {
            clear()
            geometry.polygons.forEach { polygon -> addPolygon(polygon) }

            moveCameraTo(geometry.boundary, animate = true)
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


    private fun GoogleMap.moveCameraTo(boundary: LatLngBounds, animate: Boolean = false) {
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
            boundary,
            context.dimenCompat(R.dimen.map_camera_padding)
        )

        if (animate) {
            animateCamera(cameraUpdate)
        }
        else {
            moveCamera(cameraUpdate)
        }
    }

    private inner class ElectionResultsAdapter: LoadingAdapter<ConstituencyDataHolder>() {
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
                    accent.setBackgroundColor(theme.primary)
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

                    accent.setBackgroundColor(theme.primary)
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
