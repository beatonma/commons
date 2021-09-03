package org.beatonma.commons.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.theme.CommonsTheme

@Composable
fun TestTheme(
    vararg providedValues: ProvidedValue<*>,
    content: @Composable () -> Unit
) {

    CommonsTheme {
        ProvideWindowInsets {
            CompositionLocalProvider(
                *providedValues,
                content = content
            )
        }
    }
}
