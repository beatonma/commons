package org.beatonma.commons.app.ui.compose.components.party

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import org.beatonma.commons.compose.util.withHsl
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.PathConfig
import org.beatonma.commons.svg.ScaleType

@Composable
fun ProvidePartyImageConfig(
    scaleType: ScaleType = ScaleType.Max,
    alignment: Alignment = Alignment.Center,
    scaleMultiplier: Float = 1.5F,
    offset: Offset = Offset(0.5F, 0F),
    pathConfig: PathConfig = PathConfig {
        it.withHsl {
            if (saturation > 0.1F) {
                saturation = 1F
            }
            lightness = 0.5F
        }
    },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        *providePartyImageConfig(
            scaleType, alignment, scaleMultiplier, offset, pathConfig
        ),
        content = content
    )
}


@Composable
fun providePartyImageConfig(
    scaleType: ScaleType = ScaleType.Min,
    alignment: Alignment = Alignment.CenterEnd,
    scaleMultiplier: Float = 2F,
    offset: Offset = Offset(0.5F, 0F),
    pathConfig: PathConfig = PathConfig {
        it.withHsl {
            if (saturation > 0.1F) {
                saturation = 1F
            }
            lightness = 0.5F
        }
    },
): Array<out ProvidedValue<*>> {
    val backgroundPortraitConfig = remember {
        ImageConfig(scaleType, alignment,
            scaleMultiplier = scaleMultiplier,
            offset = offset,
            pathConfig = pathConfig
        )
    }
    val partyImageCache = rememberPartyImageCache()

    return arrayOf(
        LocalImageConfig provides backgroundPortraitConfig,
        LocalPartyImageCache provides partyImageCache,
    )
}
