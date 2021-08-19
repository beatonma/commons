package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

private data class PermissionStatus(val name: String, val granted: Boolean)
class PermissionResults(
    permissions: Array<out String>,
    grantResults: IntArray,
) {
    private val permissions: List<PermissionStatus> = permissions.zip(grantResults.toTypedArray())
        .map { PermissionStatus(it.first, it.second == PackageManager.PERMISSION_GRANTED) }

    fun check(permission: String, block: (granted: Boolean) -> Unit) {
        block(permissions.firstOrNull { it.name == permission }?.granted == true)
    }
}

fun Context.hasPermission(requestedPermission: String): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        requestedPermission
    ) == PackageManager.PERMISSION_GRANTED
