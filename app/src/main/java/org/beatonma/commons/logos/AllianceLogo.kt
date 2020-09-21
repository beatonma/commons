package org.beatonma.commons.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.compose.color.CommonsColor

class AllianceLogo : VectorGraphic(
    pathCount = 1,
    width = 48,
    height = 51,
    primaryColor = CommonsColor.Political.Party.Primary.Alliance,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath(),
            color = Color.Black,
        )
    }

    private fun getPath(): Path = Path().apply {
        moveTo(23.81f, 33.82f);
        cubicTo(26.68f, 33.82f, 29.44f, 34.47f, 31.98f, 35.65f);
        lineTo(23.81f, 15.86f);
        lineTo(15.63f, 35.65f);
        cubicTo(18.18f, 34.47f, 20.93f, 33.82f, 23.81f, 33.82f);
        close();

        moveTo(47.61f, 50.29f);
        lineTo(38.63f, 50.29f);
        cubicTo(35.58f, 43.63f, 30.08f, 39.18f, 23.81f, 39.18f);
        cubicTo(17.53f, 39.18f, 12.03f, 43.63f, 8.98f, 50.29f);
        lineTo(0f, 50.29f);
        lineTo(0f, 48.06f);
        cubicTo(0f, 48.06f, 3.7f, 48.03f, 4.37f, 46.49f);
        cubicTo(7.89f, 38.37f, 23.81f, 0f, 23.81f, 0f);
        cubicTo(23.81f, 0f, 39.72f, 38.37f, 43.24f, 46.49f);
        cubicTo(43.8f, 47.77f, 46.51f, 48.03f, 47.61f, 48.06f);
    }
}
