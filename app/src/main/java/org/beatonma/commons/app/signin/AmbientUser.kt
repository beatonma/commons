package org.beatonma.commons.app.signin

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import org.beatonma.commons.data.core.room.entities.user.UserToken

val AmbientUserToken: ProvidableAmbient<UserToken> = ambientOf { NullUserToken }

// Represents a user who has not signed in.
val NullUserToken = UserToken(
    name = null, photoUrl = null, email = null,
    snommocToken = "null", googleId = "null", username = "NoUser"
)
