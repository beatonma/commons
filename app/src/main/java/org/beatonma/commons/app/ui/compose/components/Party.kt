package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.logos.PartyLogos
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.render

val AmbientPartyTheme: ProvidableAmbient<PartyWithTheme> = ambientOf {
    error("Party not set")
}
val AmbientImageConfig = ambientOf { ImageConfig() }
internal val PartyImageCache: ProvidableAmbient<MutableMap<ParliamentID, VectorGraphic>> =
    ambientOf {
        error("No image cache")
    }

data class PartyWithTheme(val party: Party, val theme: ComposePartyColors)

@Composable
fun PartyWithTheme(party: Party) = PartyWithTheme(party, party.theme())

@Composable
fun PartyBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val partyWithTheme = AmbientPartyTheme.current
    PartyBackground(
        partyWithTheme.party,
        partyWithTheme.theme,
        modifier = modifier,
        content = content
    )
}

@Composable
fun PartyBackground(
    party: Party,
    theme: ComposePartyColors,
    imageConfig: ImageConfig = AmbientImageConfig.current,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val logo = getLogo(PartyImageCache.current, party)

    PartyBackground(
        logo,
        theme,
        imageConfig,
        modifier,
        content = content,
    )
}

@Composable
fun PartyBackground(
    logo: VectorGraphic,
    theme: ComposePartyColors,
    config: ImageConfig,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier,
        color = theme.primary.copy(alpha = 0.9F)
    ) {
        PartyPortrait(
            logo,
            config,
            modifier = Modifier.fillMaxWidth()
        )

        content()
    }
}

@Composable
internal fun PartyPortrait(
    graphic: VectorGraphic,
    config: ImageConfig,
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Canvas(modifier) {
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
