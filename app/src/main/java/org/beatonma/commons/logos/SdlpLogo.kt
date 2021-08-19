package org.beatonma.commons.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.compose.color.PoliticalColor

class SdlpLogo : VectorGraphic(
    pathCount = 3,
    width = 50,
    height = 48,
    primaryColor = PoliticalColor.Party.Primary.Sdlp,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath0(),
            color = Color(0xffe31836),
        )

        paths[1] = vectorPath(
            path = getPath1(),
            color = Color(0xfff89828),
        )

        paths[2] = vectorPath(
            path = getPath2(),
            color = Color(0xff006e51),
        )
    }

    private fun getPath0(): Path = Path().apply {
        moveTo(3.57f, 42.82f)
        cubicTo(2.05f, 41.37f, 0.99f, 39.55f, 0.44f, 37.52f)
        cubicTo(-0.12f, 35.47f, -0.15f, 33.21f, 0.38f, 30.93f)
        cubicTo(2.21f, 23.1f, 6.85f, 16.75f, 14.17f, 12.04f)
        cubicTo(14.18f, 12.46f, 14.2f, 12.88f, 14.25f, 13.31f)
        cubicTo(11.82f, 16.06f, 8.72f, 20.34f, 7.54f, 25.21f)
        cubicTo(6.93f, 27.73f, 6.98f, 29.9f, 7.47f, 31.67f)
        cubicTo(8.25f, 34.55f, 10.18f, 36.4f, 12.28f, 37.06f)
        cubicTo(15.83f, 38.18f, 19.17f, 36.79f, 21.95f, 33.06f)
        cubicTo(23.11f, 31.5f, 24.06f, 29.32f, 24.56f, 28.15f)
        cubicTo(23.91f, 37.12f, 19.88f, 43.06f, 12.51f, 45.15f)
        cubicTo(9.33f, 46.06f, 6.07f, 45.21f, 3.57f, 42.82f)
    }

    private fun getPath1(): Path = Path().apply {
        moveTo(49.35f, 37.66f)
        cubicTo(48.86f, 39.7f, 47.81f, 41.53f, 46.34f, 43.02f)
        cubicTo(44.84f, 44.53f, 42.9f, 45.69f, 40.66f, 46.37f)
        cubicTo(32.98f, 48.69f, 25.16f, 47.84f, 17.42f, 43.85f)
        cubicTo(17.78f, 43.63f, 18.13f, 43.4f, 18.48f, 43.15f)
        cubicTo(22.08f, 43.87f, 27.33f, 44.43f, 32.13f, 43.02f)
        cubicTo(34.61f, 42.28f, 36.46f, 41.16f, 37.75f, 39.86f)
        cubicTo(39.85f, 37.74f, 40.49f, 35.14f, 40.01f, 32.98f)
        cubicTo(39.2f, 29.35f, 36.33f, 27.15f, 31.71f, 26.6f)
        cubicTo(29.77f, 26.38f, 27.25f, 26.67f, 25.99f, 26.8f)
        cubicTo(34.08f, 22.9f, 41.4f, 23.41f, 46.9f, 28.75f)
        cubicTo(49.27f, 31.05f, 50.17f, 34.3f, 49.35f, 37.66f)
    }

    private fun getPath2(): Path = Path().apply {
        moveTo(21.88f, 0.45f)
        cubicTo(23.89f, -0.14f, 26f, -0.14f, 28.03f, 0.39f)
        cubicTo(30.08f, 0.94f, 32.06f, 2.03f, 33.76f, 3.63f)
        cubicTo(39.62f, 9.14f, 42.79f, 16.34f, 43.22f, 25.04f)
        cubicTo(42.85f, 24.84f, 42.47f, 24.65f, 42.08f, 24.48f)
        cubicTo(40.9f, 21f, 38.76f, 16.17f, 35.13f, 12.71f)
        cubicTo(33.26f, 10.92f, 31.36f, 9.88f, 29.58f, 9.41f)
        cubicTo(26.7f, 8.65f, 24.14f, 9.4f, 22.51f, 10.89f)
        cubicTo(19.78f, 13.4f, 19.3f, 16.99f, 21.14f, 21.27f)
        cubicTo(21.91f, 23.06f, 23.35f, 25.16f, 24.1f, 26.18f)
        cubicTo(16.66f, 21.12f, 13.53f, 14.47f, 15.4f, 7.04f)
        cubicTo(16.2f, 3.82f, 18.57f, 1.43f, 21.88f, 0.45f)
    }
}
