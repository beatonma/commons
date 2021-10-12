package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.LocalWindowInsets


val statusBarHeight: Int @Composable get() = LocalWindowInsets.current.statusBars.top
