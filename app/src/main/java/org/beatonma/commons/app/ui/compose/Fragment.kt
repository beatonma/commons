package org.beatonma.commons.app.ui.compose

import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.Providers
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.components.FeedbackLayout
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.SystemBars
import org.beatonma.commons.theme.compose.theme.systemui.withSystemUi

fun Fragment.composeScreen(
    vararg providers: ProvidedValue<*>,
    content: @Composable () -> Unit,
) = ComposeView(requireContext()).apply {
    setContent {
        CommonsTheme {
            Providers(
                *providers,
                AmbientContentColor provides colors.onBackground,
            ) {
                WithSystemUi {
                    FeedbackLayout(content = content)
                }
            }
        }
    }
}

@Composable
private fun Fragment.WithSystemUi(
    systemBarColor: Color = MaterialTheme.colors.SystemBars,
    content: @Composable () -> Unit,
) = withSystemUi(
    requireActivity().window,
    systemBarColor,
    content
)
