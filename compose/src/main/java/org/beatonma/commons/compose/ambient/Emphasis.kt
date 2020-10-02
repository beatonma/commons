package org.beatonma.commons.compose.ambient

import androidx.compose.material.EmphasisAmbient
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable

@Composable
fun withEmphasisHigh(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = EmphasisAmbient.current.high, content = content)

@Composable
fun withEmphasisMedium(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = EmphasisAmbient.current.medium, content = content)

@Composable
fun withEmphasisDisabled(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled, content = content)
