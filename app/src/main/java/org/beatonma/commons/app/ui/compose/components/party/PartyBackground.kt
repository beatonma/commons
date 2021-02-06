package org.beatonma.commons.app.ui.compose.components.party

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.logos.PartyLogos
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.render


val AmbientImageConfig = ambientOf { ImageConfig() }
internal val AmbientPartyImageCache: ProvidableAmbient<MutableMap<ParliamentID, VectorGraphic>> =
    ambientOf {
        error("No image cache")
    }

@Composable
fun rememberPartyImageCache() = remember { mutableMapOf<ParliamentID, VectorGraphic>() }

@Composable
fun PartyBackground(
    modifier: Modifier = Modifier,
    partyWithTheme: PartyWithTheme = AmbientPartyTheme.current,
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
    party: Party,
    modifier: Modifier = Modifier,
    imageConfig: ImageConfig = AmbientImageConfig.current,
    theme: ComposePartyColors = party.theme(),
    useCache: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    val logo = if (useCache) {
        getLogo(AmbientPartyImageCache.current, party)
    } else {
        remember { PartyLogos.get(party.parliamentdotuk) }
    }

    PartyBackground(
        logo,
        theme,
        imageConfig,
        modifier,
        content = content,
    )
}

@Composable
private fun PartyBackground(
    logo: VectorGraphic,
    theme: ComposePartyColors,
    config: ImageConfig,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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

@Composable
private fun PartyPortrait(
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
    party: Party,
): VectorGraphic {
    val partyId = party.parliamentdotuk

    return when {
        cache.contains(partyId) -> cache[partyId]!!

        else -> {
            val graphic = PartyLogos.get(partyId)
            cache[partyId] = graphic
            graphic
        }
    }
}
