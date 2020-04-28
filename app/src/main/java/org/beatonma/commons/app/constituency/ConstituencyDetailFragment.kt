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
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.load
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.databinding.FragmentConstituencyDetailBinding
import org.beatonma.commons.ui.colors.PartyColors
import org.beatonma.lib.util.kotlin.extensions.bindText
import org.beatonma.lib.util.kotlin.extensions.dp
import org.beatonma.lib.util.kotlin.extensions.ifPermissionAvailable

private const val TAG = "ConstituencyDetailFragment"
private const val MAPVIEW_BUNDLE_KEY = "MapViewBundle"
private const val CAMERA_PADDING_DP = 32

class ConstituencyDetailFragment : BaseViewmodelFragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var binding: FragmentConstituencyDetailBinding
    private val viewmodel: ConstituencyDetailViewModel by viewModels { viewmodelFactory }

    private val mapView: MapView? get() = binding.mapview
    private var gMap: GoogleMap? = null

    private fun getConstituencyFromBundle(): Int? {
        return arguments?.getInt(PARLIAMENTDOTUK)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parliamentdotuk = getConstituencyFromBundle()
        if (parliamentdotuk == null) {
            Log.w(TAG, "Failed to get constituency ID from bundle!")
            return
        }
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

        viewmodel.liveData.observe(viewLifecycleOwner) {
            updateConstituencyUi(it.constituency, it.theme)
            updateGeometryUi(it.geometry, it.theme)
            updateMemberUi(it.member, it.theme)
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

    private fun updateMemberUi(basicProfile: BasicProfileWithParty?, theme: PartyColors?) {
        basicProfile ?: return

        binding.mp.apply {
            bindText(
                title to basicProfile.profile.name,
                subtitle to basicProfile.profile.currentPost,
                description to basicProfile.profile.portraitUrl,
            )
            theme?.also {
                accentLine.setBackgroundColor(it.primary)
            }
            portrait.load(basicProfile.profile.portraitUrl)
        }
    }

    private fun updateMap(gMap: GoogleMap, geometry: Geometry, theme: PartyColors) {
        gMap.apply {
            clear()
            setMapStyle(viewmodel.getMapStyle())
            geometry.polygons.forEach { polygon -> addPolygon(polygon) }

            moveCameraTo(geometry.boundary)
        }
    }


    // All lifecycle events must be passed to the MapView
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

    private fun GoogleMap.moveCameraTo(boundary: LatLngBounds) {
        moveCamera(CameraUpdateFactory.newLatLngBounds(boundary, context.dp(CAMERA_PADDING_DP)))
    }
}
