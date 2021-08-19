package org.beatonma.commons.app.ui.maps

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLngBounds


fun GoogleMap.moveCameraTo(
    config: MapConfig,
    boundary: LatLngBounds = config.defaultPosition,
    animate: Boolean = false,
) = moveCameraTo(boundary, config.paddingPx, animate)

fun GoogleMap.moveCameraTo(
    boundary: LatLngBounds,
    padding: Int,
    animate: Boolean = false,
) {
    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
        boundary,
        padding
    )

    if (animate) {
        animateCamera(cameraUpdate)
    }
    else {
        moveCamera(cameraUpdate)
    }
}

/**
 * Remembers a MapView and gives it the lifecycle of the current LifecycleOwner
 *
 * From Crane sample app:
 *   https://github.com/android/compose-samples/blob/main/Crane/app/src/main/java/androidx/compose/samples/crane/details/MapViewUtils.kt
 */
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
private fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }
