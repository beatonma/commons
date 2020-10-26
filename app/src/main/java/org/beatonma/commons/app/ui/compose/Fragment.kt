package org.beatonma.commons.app.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.beatonma.commons.theme.compose.theme.CommonsTheme

fun Fragment.composeView(
    content: @Composable () -> Unit,
) = ComposeView(requireContext()).apply {
    setContent {
        CommonsTheme {
            content()
        }
    }
}
