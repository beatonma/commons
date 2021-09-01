package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester

/**
 * Calls focusRequester.requestFocus() when calculation is true.
 */
@Composable
fun RequestFocusWhen(focusRequester: FocusRequester, calculation: Boolean) {
    LaunchedEffect(calculation) {
        if (calculation) {
            focusRequester.requestFocus()
        }
    }
}

/**
 * Calls focusRequester.requestFocus() when calculation evaluates to true.
 */
@Composable
inline fun RequestFocusWhen(focusRequester: FocusRequester, block: () -> Boolean) {
    RequestFocusWhen(focusRequester, block())
}
