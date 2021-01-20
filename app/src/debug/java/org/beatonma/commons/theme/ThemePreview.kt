package org.beatonma.commons.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.compose.components.AmbientImageConfig
import org.beatonma.commons.app.ui.compose.components.PartyPortrait
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.util.*
import org.beatonma.commons.logos.PartyLogos
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.ScaleType
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.theme.compose.color.PoliticalColor
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.compose.themepreview.CustomPreviewScreen
import org.beatonma.compose.themepreview.ThemePreview

@Composable
@Preview
fun CommonsThemePreview() {
    ThemePreview(
        theme = { isDark, content -> CommonsTheme(isDark, content) },
        CustomPreviewScreen(
            "Commons",
        ) {
            AllPartyPortraits()
            Colors()
        }
    )
}

@Composable
private fun Colors() {
    val colors = remember {
        PoliticalColor.Party.Primary.all()
            .shuffled()
    }

    Column {
        colors.forEach {
            ColorRow(it)
        }
    }
//    LazyColumnFor(colors) {
//        ColorRow(it)
//    }
}

@Composable
private fun ColorPatch(c: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier.padding(16.dp)
            .size(160.dp)
            .shadow(4.dp, shapes.medium),
        color = c,
        content = content
    )
}

@Composable
private fun ColorRow(c: Color) {
    val converted = c.toHslColor().toColor()

    val canonicalColorHex = c.toPrettyHexString()
    Row {
        ColorPatch(c) {
            Text(
                canonicalColorHex,
                Modifier.padding(16.dp),
                fontFamily = FontFamily.Monospace,
                color = c.contentColor(),
            )
        }

        val convertedColorHex = converted.toPrettyHexString()
        ColorPatch(converted) {
            Text(
                convertedColorHex.diff(canonicalColorHex),
                Modifier.padding(16.dp),
                fontFamily = FontFamily.Monospace,
                color = converted.contentColor(),
            )
        }
    }
}

@Composable
private fun AllPartyPortraits() {
    val partyLogos = remember { PartyLogos.all().shuffled() }
    val portraitConfig =
        remember { ImageConfig(ScaleType.Min, Alignment.Center, 1.0F) }

    Providers(AmbientImageConfig provides portraitConfig) {
        PartyPortraits(partyLogos)
    }
}

@Composable
private fun PartyPortraits(
    logos: List<VectorGraphic>,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
) {

    Column {
        logos.forEach { logo ->
            Surface(
                Modifier
                    .padding(16.dp)
                    .shadow(4.dp, shapes.small),
                color = Color.LightGray
            ) {
                PartyPortrait(
                    logo,
                    AmbientImageConfig.current,
                    modifier = modifier.size(size)
                )
            }
        }
    }
}

/**
 * Only for short strings!
 */
@Composable
fun CharSequence.diff(other: CharSequence) = buildAnnotatedString {
    val differenceStyle = SpanStyle(
        textDecoration = TextDecoration.Underline,
    )

    this@diff.forEachIndexed { index, c ->
        val otherC = other.getOrNull(index)
        if (c == otherC) {
            append(c)
        }
        else {
            withStyle(differenceStyle) {
                append(c)
            }
        }
    }
}
