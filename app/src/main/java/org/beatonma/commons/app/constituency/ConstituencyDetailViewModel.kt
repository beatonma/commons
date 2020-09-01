package org.beatonma.commons.app.constituency

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.getTheme
import org.beatonma.commons.context
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.IoResultObserver
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.BasicProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.data.parse.KmlParser
import org.beatonma.commons.kotlin.data.Color
import org.beatonma.commons.kotlin.extensions.dp
import org.beatonma.commons.kotlin.extensions.stringCompat
import org.beatonma.commons.repo.repository.ConstituencyRepository

private const val MAP_OUTLINE_WIDTH_DP = 2F

class ConstituencyDetailViewModel @ViewModelInject constructor(
    private val repository: ConstituencyRepository,
    @ApplicationContext application: Context,
): AndroidViewModel(application.app) {

    private lateinit var constituencyLiveData: LiveDataIoResult<CompleteConstituency>
    private val geometryLiveData: MutableLiveData<Geometry> = MutableLiveData()

    internal val liveData = MediatorLiveData<ConstituencyData>()

    private val geometryObserver = IoResultObserver<CompleteConstituency> {
        buildGeometry(it.data?.boundary)
    }

    private var constituencyData = ConstituencyData()
        set(value) {
            field = value
            liveData.value = field
        }


    fun forConstituency(parliamentdotuk: ParliamentID) {
        constituencyLiveData = repository.getConstituency(parliamentdotuk).asLiveData()
        constituencyLiveData.observeForever(geometryObserver)

        liveData.apply {
            addSource(constituencyLiveData) { result ->
                val data = result.data ?: return@addSource

                val electionResults = data.electionResults
                    ?.sortedByDescending {
                        it.election.date
                    }

                val party = electionResults?.firstOrNull()?.party
                val theme = party?.getTheme(context)

                constituencyData = constituencyData.copy(
                    constituency = data.constituency,
                    party = party,
                    theme = theme,
                    electionResults = electionResults?.composeWithHeaders(context),
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
        include(LatLng(60.86, -8.45))  // North west 'corner', close to Faroe Islands
        include(LatLng(49.86, 1.78))  // South east 'corner', near Amiens, France
    }.build()


    private fun buildGeometry(boundary: ConstituencyBoundary?) {
        boundary ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val geometry = KmlParser().parse(boundary.kml.byteInputStream())
            geometryLiveData.postValue(geometry)
        }
    }

    private fun Geometry.setStyle(theme: PartyColors) {
        setStyle(
            Color(theme.primary).alpha(.15F).color,
            Color(theme.accent).alpha(.20F).color,
            context.dp(MAP_OUTLINE_WIDTH_DP)
        )
    }
}


internal data class ConstituencyData(
    val constituency: Constituency? = null,
    val party: Party? = null,
    val electionResults: List<ConstituencyDataHolder>? = null,
    val geometry: Geometry? = null,
    val theme: PartyColors? = null,
)


internal sealed class ConstituencyDataHolder

internal data class ConstituencyResultData(
    val election: Election,
    val profile: BasicProfile,
    val party: Party
): ConstituencyDataHolder()

internal data class ConstituencyFirstResultData(
    val election: Election,
    val profile: BasicProfile,
    val party: Party
): ConstituencyDataHolder()

internal data class ConstituencyHeaderData(
    val title: String
): ConstituencyDataHolder()


/**
 * Rebuild the list with titles etc
 */
private fun List<ConstituencyResultWithDetails>.composeWithHeaders(
    context: Context,
): List<ConstituencyDataHolder> {
    val currentMember = firstOrNull()?.let {
        ConstituencyFirstResultData(it.election, it.profile, it.party)
    } ?: return listOf()
    val previousMembers = drop(1).map {
        ConstituencyResultData(it.election, it.profile, it.party)
    }

    return mutableListOf(
        ConstituencyHeaderData(context.stringCompat(R.string.constituency_current_member)),
        currentMember,
        ConstituencyHeaderData(context.stringCompat(R.string.constituency_previous_members)),
    ).apply { addAll(previousMembers) }
}
