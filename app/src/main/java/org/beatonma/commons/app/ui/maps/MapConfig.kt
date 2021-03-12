package org.beatonma.commons.app.ui.maps

import android.content.Context
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.android.libraries.maps.model.MapStyleOptions
import org.beatonma.commons.R
import org.beatonma.commons.compose.util.px
import org.beatonma.commons.compose.util.pxF

val LocalMapConfig: ProvidableCompositionLocal<MapConfig> = compositionLocalOf {
    error("AmbientMapStyle Not set")
}

fun Context.defaultMapConfig(
    mapStyleOptions: MapStyleOptions = loadMapStyle(this),
    defaultPosition: LatLngBounds = getUkBounds(),
    paddingDp: Dp = 64.dp,
    outlineThicknessDp: Dp = 2.dp
) = MapConfig(
    mapStyleOptions,
    defaultPosition,
    paddingPx = paddingDp.px(this),
    outlineThicknessPx = outlineThicknessDp.pxF(this)
)

data class MapConfig(
    val mapStyleOptions: MapStyleOptions,
    val defaultPosition: LatLngBounds = getUkBounds(),
    val paddingPx: Int,
    val outlineThicknessPx: Float,
)

fun loadMapStyle(context: Context): MapStyleOptions =
    MapStyleOptions.loadRawResourceStyle(context, R.raw.google_maps_style)

private fun getUkBounds(): LatLngBounds = LatLngBounds.Builder().apply {
    include(LatLng(60.86, -8.45))  // North west 'corner', close to Faroe Islands
    include(LatLng(49.86, 1.78))  // South east 'corner', near Amiens, France
}.build()
