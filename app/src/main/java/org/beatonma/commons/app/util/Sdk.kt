package org.beatonma.commons.app.util

import android.os.Build

object Sdk {
    val isR: Boolean
        get() = atLeast(Build.VERSION_CODES.R)

    /**
     * Return true if device is running Android 10 (29) or later
     */
    val isQ: Boolean
        get() = atLeast(Build.VERSION_CODES.Q)

    /**
     * Return true if device is running Android Pie (28) or later
     */
    val isPie: Boolean
        get() = atLeast(Build.VERSION_CODES.P)

    /**
     * Return true if device is running Android Oreo (26) or later
     */
    val isOreo: Boolean
        get() = atLeast(Build.VERSION_CODES.O)

    /**
     * Return true if device is running Android Nougat (24) or later
     */
    val isNougat: Boolean
        get() = atLeast(Build.VERSION_CODES.N)

    /**
     * Return true if device is running Android Marshmallow (23) or later
     */
    val isMarshmallow: Boolean
        get() = atLeast(Build.VERSION_CODES.M)

    /**
     * Return true if device is running Android Lollipop (21) or later
     */
    val isLollipop: Boolean
        get() = atLeast(Build.VERSION_CODES.LOLLIPOP)

    /**
     * Return true if device is running Android Kitkat (19) or later
     */
    val isKitkat: Boolean
        get() = atLeast(Build.VERSION_CODES.KITKAT)

    val version: Int
        get() = Build.VERSION.SDK_INT

    /**
     * Return true if device is running a version of Android that is at least as recent as [sdkLevel]
     */
    fun atLeast(sdkLevel: Int) = Build.VERSION.SDK_INT >= sdkLevel
}
