package org.beatonma.commons.app.signin.compose

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.signin.RenameResult
import org.beatonma.commons.data.core.room.entities.user.UserToken

val AmbientUserToken: ProvidableAmbient<UserToken> = ambientOf { NullUserToken }

// Represents a user who has not signed in.
val NullUserToken = UserToken(
    name = null, photoUrl = null, email = null,
    snommocToken = "null", googleId = "null", username = "NoUser"
)

class UserAccountActions(
    val signIn: ActionBlock = {},
    val signOut: ActionBlock = {},
    val renameAccount: suspend (userToken: UserToken, newName: String) -> RenameResult =
        { _, _ -> TODO("renameAccount is not implemented") },
    val deleteAccount: suspend (userToken: UserToken) -> Unit =
        { TODO("deleteAccount is not implemented") },
)
