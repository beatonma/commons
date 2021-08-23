package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.compose.color.PoliticalColor

class SnpLogo : VectorGraphic(
    pathCount = 1,
    width = 1261,
    height = 1427,
    primaryColor = PoliticalColor.Party.Primary.Snp,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath(),
            color = Color.Black,
        )
    }

    private fun getPath(): Path = Path().apply {
        moveTo(291.26f, 939.7f)
        cubicTo(291.26f, 939.7f, 222.26f, 1026.7f, 281.26f, 1146.7f)
        cubicTo(352.26f, 1266.7f, 450.26f, 1336.7f, 628.26f, 1336.7f)
        cubicTo(806.26f, 1336.7f, 929.26f, 1256.7f, 979.26f, 1146.7f)
        cubicTo(1029.26f, 1026.7f, 959.26f, 944.7f, 959.26f, 944.7f)
        lineTo(642.26f, 588.7f)
        close()

        moveTo(39.26f, 0f)
        lineTo(371.26f, 0f)
        lineTo(632.26f, 256.7f)
        lineTo(899.26f, 0f)
        lineTo(1229.26f, 0f)
        lineTo(791.26f, 425.7f)
        lineTo(1159.26f, 815.7f)
        cubicTo(1159.26f, 815.7f, 1299.26f, 956.7f, 1249.26f, 1106.7f)
        cubicTo(1189.26f, 1236.7f, 1019.26f, 1426.7f, 632.26f, 1426.7f)
        cubicTo(247.26f, 1426.7f, 39.26f, 1206.7f, 9.26f, 1096.7f)
        cubicTo(-19.74f, 1006.7f, 24.26f, 885.7f, 73.26f, 815.7f)
        cubicTo(129.26f, 736.7f, 465.26f, 414.7f, 465.26f, 414.7f)
        lineTo(39.26f, 0f)
    }
}
