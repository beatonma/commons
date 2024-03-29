package org.beatonma.commons.app.ui.components.party

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.partyTheme
import org.beatonma.commons.app.ui.logos.PartyLogos
import org.beatonma.commons.app.util.logDebug
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.render


val LocalImageConfig = compositionLocalOf { ImageConfig() }
internal val LocalPartyImageCache: ProvidableCompositionLocal<MutableMap<ParliamentID, VectorGraphic>?> =
    compositionLocalOf {
        logDebug("No image cache provided")
        null
    }

@Composable
fun rememberPartyImageCache() = remember { mutableMapOf<ParliamentID, VectorGraphic>() }

/**
 * PartyBackground which uses the current AmbientPartyTheme.
 */
@Composable
fun PartyBackground(
    modifier: Modifier = Modifier,
    partyWithTheme: PartyWithTheme = LocalPartyTheme.current,
    content: @Composable () -> Unit,
) {
    PartyBackground(
        partyWithTheme.party,
        modifier = modifier,
        theme = partyWithTheme.theme,
        content = content
    )
}

/**
 * @param useCache Should be true if several PartyBackgrounds are being displayed together.
 *                 Should be false if this is the only instance.
 */
@Composable
fun PartyBackground(
    partyId: ParliamentID,
    modifier: Modifier = Modifier,
    imageConfig: ImageConfig = LocalImageConfig.current,
    theme: PartyColors = partyTheme(partyId),
    cache: MutableMap<ParliamentID, VectorGraphic>? = LocalPartyImageCache.current,
    content: @Composable () -> Unit = {},
) {
    val logo = cache?.let { getLogo(it, partyId) } ?: remember { PartyLogos.get(partyId) }

    PartyBackground(
        logo,
        theme,
        imageConfig,
        modifier,
        content = content,
    )
}

/**
 * @param useCache Should be true if several PartyBackgrounds are being displayed together.
 *                 Should be false if this is the only instance.
 */
@Composable
fun PartyBackground(
    party: Party,
    modifier: Modifier = Modifier,
    imageConfig: ImageConfig = LocalImageConfig.current,
    theme: PartyColors = party.theme(),
    cache: MutableMap<ParliamentID, VectorGraphic>? = LocalPartyImageCache.current,
    content: @Composable () -> Unit = {},
) = PartyBackground(party.parliamentdotuk, modifier, imageConfig, theme, cache, content)

@Composable
private fun PartyBackground(
    logo: VectorGraphic,
    theme: PartyColors,
    config: ImageConfig,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides theme.onPrimary,
    ) {
        Layout(
            content = {
                PartyPortrait(
                    logo,
                    config,
                )

                Surface(
                    color = theme.primary.copy(alpha = .9F),
                    content = content
                )
            },
            modifier = modifier.background(colors.surface),
        ) { measurables, constraints ->
            require(measurables.size == 2)

            // Use the size of content to determine size of PartyPortrait
            // then draw the portrait behind content.

            val contentPlaceable = measurables[1].measure(constraints)
            val width: Int = contentPlaceable.width
            val height: Int = contentPlaceable.height

            val portraitPlaceable = measurables[0].measure(
                Constraints.fixed(width, height)
            )

            layout(width, height) {
                portraitPlaceable.placeRelative(0, 0)
                contentPlaceable.placeRelative(0, 0)
            }
        }
    }
}

@Composable
internal fun PartyPortrait(
    graphic: VectorGraphic,
    config: ImageConfig,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier.clipToBounds()
    ) {
        graphic.render(this@Canvas, config)
    }
}


private fun getLogo(
    cache: MutableMap<ParliamentID, VectorGraphic>,
    partyId: ParliamentID,
): VectorGraphic =
    when {
        cache.contains(partyId) -> cache[partyId]!!

        else -> {
            val graphic = PartyLogos.get(partyId)
            cache[partyId] = graphic
            graphic
        }
    }
