package org.beatonma.commons.app.constituency.compose.detail

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.maps.MapView
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.ProvideSocial
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.StickySocialScaffold
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.party.AmbientPartyTheme
import org.beatonma.commons.app.ui.compose.components.party.PartyBackground
import org.beatonma.commons.app.ui.compose.components.party.partyWithTheme
import org.beatonma.commons.app.ui.compose.components.party.providePartyImageConfig
import org.beatonma.commons.app.ui.maps.AmbientMapConfig
import org.beatonma.commons.app.ui.maps.MapConfig
import org.beatonma.commons.app.ui.maps.moveCameraTo
import org.beatonma.commons.app.ui.maps.rememberMapViewWithLifecycle
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.parse.Geometry
import org.beatonma.commons.data.parse.KmlParser
import org.beatonma.commons.kotlin.extensions.hasPermission
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.components.ComponentTitle
import org.beatonma.commons.theme.compose.components.ScreenTitle

internal val AmbientConstituencyActions: ProvidableAmbient<ConstituencyDetailActions> =
    ambientOf { ConstituencyDetailActions() }

@Composable
fun ConstituencyDetailLayout(
    viewmodel: ConstituencyDetailViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { constituencyData ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel
        ) {
            ConstituencyDetailLayout(constituencyData)
        }
    }
}


@Composable
fun ConstituencyDetailLayout(
    data: CompleteConstituency,
    onMemberClick: ConstituencyMemberAction = AmbientConstituencyActions.current.onMemberClick,
    onConstituencyResultClick: ConstituencyResultsAction = AmbientConstituencyActions.current.onConstituencyResultsClick,
) {
    Providers(
        *providePartyImageConfig(),
        AmbientPartyTheme provides partyWithTheme(data.member?.party),
    ) {
        StickySocialScaffold(
            headerContentAboveSocial = { headerExpansion: Float, modifier: Modifier ->
                Header(data, headerExpansion, modifier)
            },
            headerContentBelowSocial = { headerExpansion: Float, modifier: Modifier ->

            },
            lazyListContent = {
                MPs(data.constituency, data.member, data.electionResults, onMemberClick, onConstituencyResultClick)
            }
        )
    }
}

@Composable
private fun Header(
    data: CompleteConstituency,
    expansion: Float,
    modifier: Modifier,
) {
    val constituency = data.constituency

    Column {
        ConstituencyMap(
            data.boundary,
            modifier
                .fillMaxWidth()
                .aspectRatio(3F / 2F)
        )
        ScreenTitle(constituency.name)
    }
}

@Composable
private fun ConstituencyMap(boundary: ConstituencyBoundary, modifier: Modifier = Modifier) {
    val map = rememberMapViewWithLifecycle()

    var geometry by remember { mutableStateOf<Geometry?>(null) }
    var job by remember { mutableStateOf<Job?>(null) }

    val coroutineScope = rememberCoroutineScope()
    if (geometry == null && job == null) {
        job = coroutineScope.launch {
            geometry = KmlParser().parse(boundary.kml.byteInputStream())
        }
    }

    MapContainer(map, geometry, modifier)
}

@Composable
private fun MapContainer(
    map: MapView,
    geometry: Geometry?,
    modifier: Modifier = Modifier,
    mapConfig: MapConfig = AmbientMapConfig.current,
    theme: ComposePartyColors = AmbientPartyTheme.current.theme,
) {
    val coroutineScope = rememberCoroutineScope()
    var locked by rememberBoolean(false)

    Box(modifier) {
        AndroidView(
            viewBlock = { map }
        ) { mapView ->
            if (locked) return@AndroidView

            coroutineScope.launch {
                val googleMap = mapView.awaitMap()

                @SuppressLint("MissingPermission")
                if (mapView.context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = false
                    }
                }

                googleMap.apply {
                    clear()
                    setMapStyle(mapConfig.mapStyleOptions)

                    if (geometry == null) {
                        moveCameraTo(mapConfig)
                    } else {
                        geometry.setStyle(theme, mapConfig)
                        geometry.polygons.forEach(::addPolygon)
                        moveCameraTo(boundary = geometry.boundary,
                            config = mapConfig,
                            animate = true)
                        locked = true
                    }
                }
            }
        }
    }
}

private fun LazyListScope.MPs(
    constituency: Constituency,
    currentMember: ConstituencyResultWithDetails?,
    previousMembers: List<ConstituencyResultWithDetails>,
    onMemberClick: ConstituencyMemberAction,
    onConstituencyResultClick: ConstituencyResultsAction,
) {
    if (currentMember != null) {
        item {
            PartyBackground(party = currentMember.party) {
                ListItem(
                    text = { Text(dotted(currentMember.profile.name, currentMember.party.name)) },
                    overlineText = { Text(currentMember.election.name) },
                    secondaryText = { OptionalText(text = currentMember.profile.currentPost) },
                    modifier = Modifier.clickable {
                        onConstituencyResultClick(constituency, currentMember)
                    }
                )
            }
        }
    }

    if (previousMembers.isNotEmpty()) {
        item {
            ComponentTitle(stringResource(R.string.constituency_previous_members))
        }

        items(previousMembers) { formerMember ->
            ListItem(
                text = { Text(dotted(formerMember.profile.name, formerMember.party.name)) },
                overlineText = { Text(formerMember.election.name) },
                secondaryText = { OptionalText(text = formerMember.profile.currentPost) },
                modifier = Modifier.clickable {
                    onConstituencyResultClick(constituency, formerMember)
                }
            )
        }
    }
}

private fun Geometry.setStyle(colors: ComposePartyColors, config: MapConfig) =
    setStyle(
        colors.primary.copy(alpha = .15F).toArgb(),
        colors.accent.copy(alpha = .20F).toArgb(),
        config.outlineThicknessPx
    )
