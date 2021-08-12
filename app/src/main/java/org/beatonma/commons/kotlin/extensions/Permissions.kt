package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermission(requestedPermission: String): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        requestedPermission
    ) == PackageManager.PERMISSION_GRANTED
