package org.beatonma.commons.network.core

import org.beatonma.commons.network.core.BuildConfig.*
import android.os.Build as Device

val userAgent: String by lazy {
    "$USER_AGENT_APP/$GIT_SHA ($USER_AGENT_WEBSITE $USER_AGENT_EMAIL) " +
            "Android[${Device.MANUFACTURER}/${Device.PRODUCT}] " +
            "${Device.VERSION.RELEASE}/${Device.VERSION.SDK_INT}/${APPLICATION_ID}v$VERSION_CODE"
}
