package org.beatonma.commons.app.signin

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.data.core.room.entities.user.UserToken

val LocalUserToken: ProvidableCompositionLocal<UserToken> = compositionLocalOf { NullUserToken }

// Represents a user who has not signed in.
val NullUserToken = UserToken(
    name = null, photoUrl = null, email = null,
    snommocToken = "null", googleId = "null", username = "NoUser"
)

/**
 * Actions that involve and affect the service account of the user.
 */
class UserAccountActions(
    val signIn: ActionBlock = {},
    val signOut: ActionBlock = {},
    val renameAccount: suspend (userToken: UserToken, newName: String) -> RenameResult =
        { _, _ -> TODO("renameAccount is not implemented") },
    val deleteAccount: suspend (userToken: UserToken) -> Unit =
        { TODO("deleteAccount is not implemented") },
)
