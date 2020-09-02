package org.beatonma.commons.kotlin.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.beatonma.commons.ActionBlock

/**
 * If the requested permissions are already granted then run the block
 * Otherwise request the permissions.
 * NOTE: If permissions are not available then the block will not be run and must be called in
 * the [Activity.onRequestPermissionsResult] callback
 * @param dontAsk   If true, user will not be asked to enable the permission.
 *                  The block will only run if the user has already allowed permission,
 *                  otherwise it will fail silently
 */


private data class PermissionStatus(val name: String, val granted: Boolean)
class PermissionResults(
    permissions: Array<out String>,
    grantResults: IntArray,
) {
    private val permissions: List<PermissionStatus> = permissions.zip(grantResults.toTypedArray())
        .map { PermissionStatus(it.first, it.second == PackageManager.PERMISSION_GRANTED) }

    fun check(permission: String, block: (Boolean) -> Unit) {
        block(permissions.firstOrNull { it.name == permission }?.granted == true)
    }
}

/**
 * Run the given block if we already have the requested permission.
 */
@SuppressWarnings("MissingPermission") inline fun Context.ifPermissionAvailable(
    requestedPermission: String,
    @SuppressWarnings("MissingPermission") block: ActionBlock
) {
    if (ContextCompat.checkSelfPermission(this, requestedPermission) == PackageManager.PERMISSION_GRANTED) {
        try {
            block()
        } catch (e: SecurityException) {}
    }
}

inline fun Context.ifPermissionsAvailable(
    permissions: Array<String>,
    block: ActionBlock
) {
    permissions.forEach { deniedPermission ->
        if (ContextCompat.checkSelfPermission(this, deniedPermission) != PackageManager.PERMISSION_GRANTED) {
            return@ifPermissionsAvailable
        }
    }

    try {
        block()
    } catch(e: SecurityException) {}
}


inline fun Context.withPermission(
    permission: String,
    requestCode: Int,
    rationale: PermissionRationale? = null,
    dontAsk: Boolean = false,
    runIfAllowed: ActionBlock
) = withPermissions(arrayOf(permission), requestCode, rationale, dontAsk, runIfAllowed)

inline fun Context.withPermissions(
    permissions: Array<String>,
    requestCode: Int,
    rationale: PermissionRationale? = null,
    dontAsk: Boolean = false,
    runIfAllowed: ActionBlock
) {
    val notGranted = mutableListOf<String>()
    permissions.forEach { deniedPermission ->
        if (ContextCompat.checkSelfPermission(this, deniedPermission) != PackageManager.PERMISSION_GRANTED) {
            notGranted += deniedPermission
        }
    }

    if (notGranted.isEmpty()) {
        try {
            runIfAllowed()
        } catch (e: SecurityException) {
            Log.e(autotag, "$e")
        }
    }
    else if (dontAsk) {
        Log.i(autotag, "withPermissions(requestCode=$requestCode, rationale=$rationale) did not run and did not ask for new permissions")
        return
    }
    else {
        if (this is Activity) {
            Log.d(autotag, "Requesting permissions - result must be handled in onRequestPermissionsResult callback")
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), requestCode)
        }
        else {
            Log.w(autotag, "Permissions [${notGranted.joinToString()}] are not granted and cannot be requested from this context")
        }
    }
}

class PermissionRationale(val title: String, val message: String) {
    fun show(context: Context, sourceView: View? = null) {
        // TODO: 22/04/2020
        Log.i("Permissions.kt", "TODO Need to show dialog to explain requested permissions.")
    }
}
