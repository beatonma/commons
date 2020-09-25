package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val default = Typography()

val CommonsTypography = Typography(
    h1 = default.h1.copy(fontSize = 60.sp, fontWeight = FontWeight.Black),
    h2 = default.h2.copy(fontSize = 48.sp),
    h3 = default.h3.copy(fontSize = 42.sp)
)
