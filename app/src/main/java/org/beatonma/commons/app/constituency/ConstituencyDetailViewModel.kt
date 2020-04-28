package org.beatonma.commons.app.constituency

import android.content.Context
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.R
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.repository.CommonsRepository
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyWithBoundary
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.data.parse.KmlParser
import org.beatonma.commons.kotlin.data.Color
import org.beatonma.commons.ui.colors.PartyColors
import org.beatonma.commons.ui.colors.getTheme
import org.beatonma.lib.util.kotlin.extensions.dp
import javax.inject.Inject


private const val MAP_OUTLINE_WIDTH_DP = 2F

class ConstituencyDetailViewModel @Inject constructor(
    private val repository: CommonsRepository,
    application: CommonsApplication,
): AndroidViewModel(application) {
    val context: Context
        get() = getApplication()

    private lateinit var constituencyLiveData: LiveData<IoResult<ConstituencyWithBoundary>>
    private lateinit var memberLiveData: LiveData<IoResult<BasicProfileWithParty>>
    private val geometryLiveData: MutableLiveData<Geometry> = MutableLiveData()

    val liveData = MediatorLiveData<ConstituencyData>()

    private val geometryObserver = Observer<IoResult<ConstituencyWithBoundary>> {
        buildGeometry(it.data?.boundary)
    }

    private var constituencyData = ConstituencyData()
        set(value) {
            field = value
            liveData.value = field
        }


    fun forConstituency(parliamentdotuk: Int) {
        constituencyLiveData = repository.observeConstituency(parliamentdotuk)
        memberLiveData = repository.observeMemberForConstituency(parliamentdotuk)

        constituencyLiveData.observeForever(geometryObserver)

        liveData.apply {
            addSource(constituencyLiveData) { result ->
                constituencyData = constituencyData.copy(constituency = result?.data?.constituency)
            }

            addSource(memberLiveData) { result ->
                val theme = result.data?.party?.getTheme(context)?.also {
                    constituencyData.geometry?.setStyle(it)
                }

                constituencyData = constituencyData.copy(
                    member = result.data,
                    theme = theme
                )
            }

            addSource(geometryLiveData) { geometry ->
                constituencyData.theme?.also { theme ->
                    geometry.setStyle(theme)
                }
                constituencyData = constituencyData.copy(geometry = geometry)
            }
        }
    }

    override fun onCleared() {
        constituencyLiveData.removeObserver(geometryObserver)

        super.onCleared()
    }

    fun getMapStyle(): MapStyleOptions =
        MapStyleOptions.loadRawResourceStyle(context, R.raw.google_maps_style)

    fun getUkBounds(): LatLngBounds = LatLngBounds.Builder().apply {
        include(LatLng(60.86, -8.45))
        include(LatLng(49.86, 1.78))
    }.build()


    private fun buildGeometry(boundary: ConstituencyBoundary?) {
        boundary ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val geometry = KmlParser().parse(boundary.kml.byteInputStream())
            geometryLiveData.postValue(geometry)
        }
    }

    data class ConstituencyData(
        val constituency: Constituency? = null,
        val member: BasicProfileWithParty? = null,
        val geometry: Geometry? = null,
        val theme: PartyColors? = null,
    )

    private fun Geometry.setStyle(theme: PartyColors) {
        setStyle(
            Color(theme.primary).alpha(.15F).color,
            Color(theme.accent).alpha(.20F).color,
            context.dp(MAP_OUTLINE_WIDTH_DP)
        )
    }
}
