package org.beatonma.commons.app.preview

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.app.search.LocalSearchActions
import org.beatonma.commons.app.search.SearchActions
import org.beatonma.commons.app.signin.LocalUserToken
import org.beatonma.commons.app.social.LocalSocialActions
import org.beatonma.commons.app.social.LocalSocialContent
import org.beatonma.commons.app.social.LocalSocialTheme
import org.beatonma.commons.app.social.LocalSocialUiState
import org.beatonma.commons.app.social.SocialActions
import org.beatonma.commons.app.social.SocialTheme
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.rememberSocialUiState
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.sampledata.SampleSocialContent
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.theme.compose.theme.CommonsTheme

@Composable
fun ProvideLocalForPreview(
    searchActions: SearchActions = remember {
        SearchActions(
            ::println,
            ::println,
        )
    },
    socialActions: SocialActions = remember { SocialActions() },
    socialUiState: MutableState<SocialUiState> = rememberSocialUiState(SocialUiState.Collapsed),
    socialContent: SocialContent = remember { SampleSocialContent },
    userToken: UserToken = remember { SampleUserToken },
    socialTheme: SocialTheme = SocialTheme(
        collapsedBackground = Color.Red,
        collapsedOnBackground = Color.White,
        expandedBackground = Color.Cyan,
        expandedOnBackground = Color.DarkGray,
    ),
    content: @Composable () -> Unit,
) {
    CommonsTheme {
        CompositionLocalProvider(
            LocalContentColor provides colors.onBackground,
            LocalSocialActions provides socialActions,
            LocalSocialUiState provides socialUiState,
            LocalUserToken provides userToken,
            LocalSearchActions provides searchActions,
            LocalSocialContent provides socialContent,
            LocalSocialTheme provides socialTheme,
            content = content
        )
    }
}
