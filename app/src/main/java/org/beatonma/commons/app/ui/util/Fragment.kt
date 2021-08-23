package org.beatonma.commons.app.ui.util

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.components.FeedbackLayout
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.SystemBars
import org.beatonma.commons.theme.compose.theme.systemui.ProvideSystemUi

fun Fragment.composeScreen(
    vararg providers: ProvidedValue<*>,
    content: @Composable BoxScope.() -> Unit,
) = ComposeView(requireContext()).apply {
    setContent {
        CommonsTheme {
            WithSystemUi {
                CompositionLocalProvider(
                    *providers,
                    LocalContentColor provides colors.onBackground,
                ) {
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
) = ProvideSystemUi(
    requireActivity().window,
    systemBarColor,
    content
)
