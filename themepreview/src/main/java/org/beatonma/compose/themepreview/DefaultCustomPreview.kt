package org.beatonma.compose.themepreview

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle

@Composable
fun DefaultCustomPreview() {
    Column(Modifier.padding(ContentPadding)) {
        val monospace = SpanStyle(fontFamily = FontFamily.Monospace)
        Text(
            AnnotatedString.Builder().apply {
                append("Pass any custom content from your app to ")

                withStyle(monospace) {
                    append("ThemePreview")
                }

                append(" with\n\n")

                withStyle(monospace) {
                    append(
                        "customScreen = CustomPreviewScreen {\n" +
                                "  /* Custom @Composable content */\n" +
                                "}"
                    )
                }
            }.toAnnotatedString()
        )
    }
}
