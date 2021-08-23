package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.compose.color.PoliticalColor

class LibDemLogo : VectorGraphic(
    pathCount = 1,
    width = 271,
    height = 213,
    primaryColor = PoliticalColor.Party.Primary.LibDem,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath(),
            color = PoliticalColor.Party.Primary.LibDem,
            style = Fill,
        )
    }

    private fun getPath(): Path = Path().apply {
        moveTo(199.47f, 79.9f)
        cubicTo(194.47f, 75.5f, 187.47f, 78.1f, 181.47f, 77.4f)
        cubicTo(171.47f, 76.3f, 160.47f, 74.9f, 151.47f, 80.9f)
        cubicTo(140.47f, 86.2f, 135.47f, 98.2f, 131.47f, 110.2f)
        cubicTo(117.47f, 143.2f, 80.07f, 165.2f, 43.87f, 161.2f)
        cubicTo(28.57f, 159.2f, 13.07f, 154.2f, 1.27f, 144.2f)
        cubicTo(-2.33f, 154.2f, 2.07f, 167.2f, 10.27f, 173.2f)
        cubicTo(23.17f, 180.2f, 38.27f, 173.2f, 51.07f, 169.2f)
        cubicTo(64.27f, 164.2f, 78.57f, 164.2f, 92.47f, 164.2f)
        cubicTo(121.47f, 166.2f, 148.47f, 146.2f, 157.47f, 119.2f)
        cubicTo(163.47f, 100.2f, 180.47f, 86.2f, 199.47f, 79.9f)
        close()

        moveTo(181.47f, 109.2f)
        cubicTo(198.47f, 99.2f, 219.47f, 109.2f, 236.47f, 100.2f)
        cubicTo(249.47f, 95.2f, 261.47f, 87.2f, 270.47f, 77.4f)
        cubicTo(271.47f, 95.2f, 255.47f, 106.2f, 242.47f, 115.2f)
        cubicTo(227.47f, 124.2f, 207.47f, 127.2f, 190.47f, 124.2f)
        cubicTo(178.47f, 123.2f, 166.47f, 125.2f, 156.47f, 132.2f)
        cubicTo(162.47f, 122.2f, 171.47f, 115.2f, 181.47f, 109.2f)
        close()

        moveTo(72.87f, 46.2f)
        cubicTo(85.47f, 52.4f, 98.47f, 57.8f, 110.47f, 65.6f)
        cubicTo(124.47f, 71.4f, 132.47f, 87.2f, 128.47f, 102.2f)
        cubicTo(126.47f, 108.2f, 118.47f, 125.2f, 117.47f, 123.2f)
        cubicTo(122.47f, 105.2f, 112.47f, 85.2f, 95.47f, 78.6f)
        cubicTo(74.17f, 69f, 49.87f, 61f, 36.97f, 40f)
        cubicTo(29.57f, 28.3f, 26.77f, 12.6f, 33.67f, 0f)
        cubicTo(38.57f, 20.6f, 54.77f, 36.4f, 72.87f, 46.2f)
        close()

        moveTo(70.47f, 86.2f)
        cubicTo(81.77f, 89.2f, 92.47f, 94.2f, 102.47f, 100.2f)
        cubicTo(114.47f, 106.2f, 114.47f, 121.2f, 109.47f, 131.2f)
        cubicTo(106.47f, 137.2f, 109.47f, 121.2f, 104.47f, 118.2f)
        cubicTo(95.47f, 108.2f, 81.67f, 106.2f, 69.67f, 103.2f)
        cubicTo(51.77f, 97.2f, 30.47f, 89.2f, 23.37f, 69.7f)
        cubicTo(21.47f, 65.1f, 19.97f, 45.7f, 21.87f, 48.3f)
        cubicTo(29.67f, 69f, 51.67f, 78f, 70.47f, 86.2f)
        close()

        moveTo(165.47f, 136.2f)
        cubicTo(179.47f, 133.2f, 192.47f, 141.2f, 206.47f, 139.2f)
        cubicTo(223.47f, 136.2f, 240.47f, 131.2f, 255.47f, 121.2f)
        cubicTo(240.47f, 144.2f, 212.47f, 157.2f, 184.47f, 155.2f)
        cubicTo(173.47f, 154.2f, 163.47f, 144.2f, 151.47f, 147.2f)
        cubicTo(137.47f, 148.2f, 157.47f, 139.2f, 162.47f, 137.2f)
        lineTo(163.47f, 137.2f)
        close()

        moveTo(89.47f, 121.2f)
        cubicTo(101.47f, 116.2f, 110.47f, 145.2f, 96.47f, 135.2f)
        cubicTo(88.47f, 133.2f, 78.97f, 135.2f, 70.47f, 132.2f)
        cubicTo(55.97f, 128.2f, 44.47f, 117.2f, 37.47f, 104.2f)
        cubicTo(52.77f, 114.2f, 70.97f, 120.2f, 89.47f, 121.2f)
        close()

        moveTo(79.57f, 167.2f)
        cubicTo(68.07f, 174.2f, 59.07f, 184.2f, 49.97f, 193.2f)
        cubicTo(42.37f, 204.2f, 25.37f, 201.2f, 18.57f, 191.2f)
        cubicTo(15.87f, 188.2f, 6.97f, 178.2f, 15.97f, 185.2f)
        cubicTo(27.77f, 190.2f, 41.57f, 185.2f, 51.67f, 178.2f)
        cubicTo(59.87f, 172.2f, 69.07f, 167.2f, 79.57f, 167.2f)
        close()

        moveTo(60.47f, 204.2f)
        cubicTo(71.47f, 194.2f, 71.67f, 177.2f, 84.47f, 169.2f)
        cubicTo(89.47f, 166.2f, 102.47f, 166.2f, 91.47f, 171.2f)
        cubicTo(83.17f, 181.2f, 86.47f, 196.2f, 77.17f, 206.2f)
        cubicTo(68.37f, 215.2f, 54.27f, 212.2f, 43.47f, 209.2f)
        cubicTo(49.47f, 209.2f, 55.47f, 207.2f, 60.47f, 204.2f)
        close()
    }
}
