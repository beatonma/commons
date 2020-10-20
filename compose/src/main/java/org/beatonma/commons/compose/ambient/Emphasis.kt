package org.beatonma.commons.compose.ambient

import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable

@Composable
fun withEmphasisHigh(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high, content = content)

@Composable
fun withEmphasisMedium(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.medium, content = content)

@Composable
fun withEmphasisDisabled(content: @Composable () -> Unit) =
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.disabled, content = content)
