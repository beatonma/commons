package org.beatonma.commons.logos

import androidx.compose.ui.graphics.Color
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.plotPath
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.compose.color.PoliticalColor

class DupLogo : VectorGraphic(
    pathCount = 2,
    width = 304,
    height = 271,
    primaryColor = PoliticalColor.Party.Primary.Dup,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath1(),
            color = Color(0xff1c1d58),
        )

        paths[1] = vectorPath(
            path = getPath2(),
            color = Color(0xffc4161d),
        )
    }

    private fun getPath1() = plotPath {
        moveTo(105.85f, 31.13f)
        cubicTo(107.98f, 38.78f, 105.17f, 46.65f, 100f, 52.18f)
        cubicTo(90.2f, 62.64f, 73.77f, 59.26f, 66.23f, 72.99f)
        cubicTo(62.17f, 84.93f, 50.69f, 97.53f, 36.96f, 98.1f)
        cubicTo(29.2f, 100f, 19.74f, 98.32f, 13.22f, 102.71f)
        cubicTo(8.82f, 109.57f, 11.19f, 119.03f, 12.65f, 126.12f)
        cubicTo(5.67f, 124.09f, 2.07f, 116.55f, 0.27f, 110.25f)
        cubicTo(-0.28f, 104.17f, -0.52f, 96.06f, 5.11f, 92.69f)
        cubicTo(17.83f, 92.57f, 28.74f, 88.07f, 40f, 84.59f)
        cubicTo(53.17f, 79.18f, 53.17f, 62.18f, 65.89f, 55.66f)
        cubicTo(76.35f, 52.4f, 88.4f, 51.49f, 95.6f, 42.38f)
        cubicTo(96.95f, 40.02f, 96.84f, 36.3f, 96.39f, 33.82f)
        cubicTo(95.49f, 31.57f, 93.24f, 29.32f, 90.99f, 28.43f)
        cubicTo(88.74f, 28.2f, 85.36f, 27.19f, 83.78f, 29.44f)
        cubicTo(83.44f, 29.44f, 82.88f, 29.67f, 82.66f, 29.21f)
        cubicTo(84.46f, 24.49f, 89.41f, 22.68f, 94.02f, 22.46f)
        cubicTo(98.86f, 23.13f, 104.61f, 25.72f, 105.85f, 31.13f)
        moveTo(227.96f, 76.14f)
        cubicTo(228.98f, 75.69f, 229.76f, 74.91f, 231f, 74.79f)
        cubicTo(231.23f, 79.29f, 226.27f, 81.1f, 222.89f, 82.67f)
        cubicTo(206.92f, 87.06f, 192.17f, 80.54f, 180.12f, 71.64f)
        cubicTo(164.81f, 63.42f, 144.1f, 59.04f, 129.37f, 71.08f)
        cubicTo(128.01f, 70.06f, 128.8f, 68.05f, 129.37f, 66.69f)
        cubicTo(138.93f, 54.87f, 152.78f, 48.68f, 167.97f, 51.61f)
        cubicTo(184.51f, 55.78f, 197.01f, 69.73f, 212.88f, 75.69f)
        cubicTo(217.6f, 77.49f, 222.89f, 76.82f, 227.96f, 76.15f)
        moveTo(249.57f, 84.81f)
        cubicTo(260.04f, 102.26f, 280.42f, 105.07f, 294.71f, 118.01f)
        cubicTo(300.22f, 124.77f, 303.26f, 132.53f, 303.37f, 141.53f)
        cubicTo(303.26f, 141.87f, 302.81f, 141.87f, 302.58f, 141.87f)
        cubicTo(292.23f, 120.04f, 265.44f, 124.32f, 253.4f, 105.07f)
        cubicTo(248f, 96.97f, 244.96f, 87.4f, 244.51f, 77.49f)
        lineTo(244.96f, 76.49f)
        cubicTo(247.66f, 78.39f, 247.54f, 82.22f, 249.57f, 84.81f)
        moveTo(101.34f, 86.16f)
        cubicTo(101.01f, 87.85f, 98.98f, 88.19f, 98.3f, 89.65f)
        cubicTo(93.24f, 93.59f, 88.17f, 96.63f, 83.78f, 101.81f)
        cubicTo(81.31f, 102.26f, 78.72f, 101.47f, 77.03f, 99.44f)
        cubicTo(74.55f, 97.42f, 75.01f, 93.82f, 74.55f, 91.33f)
        cubicTo(78.6f, 89.87f, 82.88f, 88.76f, 87.27f, 88.07f)
        cubicTo(91.33f, 86.27f, 96.05f, 87.18f, 99.43f, 85.15f)
        cubicTo(100f, 85.6f, 101.23f, 85.03f, 101.34f, 86.16f)
        moveTo(199.93f, 122.06f)
        cubicTo(210.62f, 127.69f, 225.6f, 132.08f, 236.86f, 124.77f)
        cubicTo(238.09f, 127.24f, 233.92f, 128.81f, 232.8f, 131.06f)
        cubicTo(223.8f, 137.48f, 211.53f, 139.28f, 200.72f, 135.58f)
        cubicTo(183.05f, 130.17f, 169.55f, 114.64f, 150.98f, 111.04f)
        cubicTo(148.05f, 110.92f, 145.02f, 113.62f, 142.65f, 111.82f)
        cubicTo(147.93f, 104.95f, 157.62f, 102.48f, 166.39f, 104.51f)
        cubicTo(178.33f, 108.56f, 188.45f, 116.88f, 199.93f, 122.06f)
        moveTo(41.01f, 117f)
        moveTo(41.01f, 119.36f)
        cubicTo(35.39f, 124.54f, 29.43f, 130.05f, 21.66f, 128.81f)
        cubicTo(22.45f, 127.47f, 24.47f, 126.67f, 25.93f, 125.66f)
        cubicTo(29.43f, 122.29f, 30.43f, 117.68f, 30.2f, 113.17f)
        cubicTo(34.15f, 113.85f, 37.41f, 115.87f, 41.01f, 117f)
        moveTo(134.77f, 117.56f)
        cubicTo(135.11f, 137.82f, 111.02f, 138.16f, 103.14f, 152.9f)
        cubicTo(102.46f, 153.02f, 102.02f, 152.46f, 101.34f, 152.68f)
        cubicTo(99.31f, 147.28f, 101.34f, 142.66f, 103.48f, 138.05f)
        cubicTo(111.13f, 126.34f, 127.9f, 127.69f, 132.4f, 113.96f)
        cubicTo(134.77f, 113.51f, 133.98f, 116.32f, 134.77f, 117.56f)
        moveTo(16.25f, 132.65f)
        cubicTo(15.81f, 146.37f, 18.62f, 158.98f, 28.86f, 167.98f)
        cubicTo(44.96f, 156.95f, 63.3f, 150.54f, 83.78f, 149.64f)
        cubicTo(85.14f, 149.98f, 87.5f, 150.65f, 86.15f, 152.68f)
        cubicTo(79.51f, 160.44f, 78.5f, 171.14f, 74.33f, 180.14f)
        cubicTo(69.15f, 180.37f, 66f, 175.76f, 61.27f, 174.74f)
        cubicTo(52.49f, 170.35f, 40.34f, 172.38f, 32.91f, 178f)
        cubicTo(27.17f, 183.85f, 26.05f, 192.52f, 26.72f, 199.95f)
        cubicTo(28.74f, 204.68f, 32.91f, 208.96f, 37.53f, 210.76f)
        lineTo(35.95f, 211.77f)
        cubicTo(28.97f, 210.98f, 22.32f, 207.82f, 18.39f, 201.53f)
        cubicTo(14.9f, 193.31f, 14.23f, 182.17f, 19.18f, 174.51f)
        cubicTo(8.6f, 164.27f, 8.49f, 145.92f, 14f, 133.21f)
        cubicTo(14.23f, 132.19f, 15.91f, 131.06f, 16.25f, 132.65f)
        moveTo(81.87f, 222.01f)
        cubicTo(79.17f, 239.79f, 77.37f, 259.71f, 60.48f, 269.85f)
        cubicTo(59.36f, 270.53f, 58.01f, 269.96f, 57f, 269.28f)
        cubicTo(58.8f, 266.58f, 61.95f, 265.12f, 63.19f, 261.75f)
        cubicTo(69.26f, 244.07f, 61.62f, 221.67f, 72.98f, 206.14f)
        cubicTo(65.55f, 206.14f, 58.01f, 208.84f, 51.6f, 212.89f)
        cubicTo(48.33f, 215.59f, 46.08f, 219.65f, 45.97f, 223.69f)
        lineTo(45.4f, 224.26f)
        cubicTo(43.04f, 222.23f, 44.05f, 218.41f, 43.72f, 215.25f)
        cubicTo(45.4f, 204.45f, 54.86f, 195.9f, 65.1f, 193.19f)
        cubicTo(73.65f, 190.61f, 84.79f, 192.18f, 91.89f, 184.75f)
        cubicTo(96.5f, 179.36f, 99.43f, 173.04f, 100.78f, 166.18f)
        cubicTo(106.29f, 188.35f, 85.58f, 202.32f, 81.87f, 222.01f)
    }

    private fun getPath2() = plotPath {
        moveTo(142.87f, 9.52f)
        cubicTo(168.31f, 4.79f, 194.76f, 9.85f, 213.1f, 27.3f)
        cubicTo(228.86f, 42.04f, 240.23f, 70.74f, 266.12f, 66.47f)
        cubicTo(267.58f, 66.91f, 270.4f, 66.91f, 272.31f, 66.47f)
        cubicTo(272.98f, 68.94f, 269.61f, 68.94f, 268.25f, 70.3f)
        cubicTo(264.54f, 71.53f, 260.94f, 72.76f, 256.89f, 73.22f)
        cubicTo(228.64f, 73.89f, 213.56f, 41.37f, 189.91f, 30.22f)
        cubicTo(174.5f, 21.33f, 154.91f, 19.98f, 137.24f, 21.33f)
        cubicTo(130.72f, 22.57f, 124.53f, 24.59f, 119.12f, 28.43f)
        cubicTo(118.79f, 28.31f, 118.23f, 28.54f, 117.99f, 28.09f)
        cubicTo(119.35f, 24.03f, 122.61f, 19.64f, 126.67f, 17.28f)
        cubicTo(117.43f, 10.19f, 103.03f, 9.06f, 92.34f, 14.02f)
        cubicTo(90.09f, 14.91f, 88.17f, 16.72f, 85.92f, 17.62f)
        cubicTo(86.48f, 12.1f, 91.33f, 10.07f, 94.81f, 6.26f)
        cubicTo(102.02f, 1.41f, 110.91f, -0.84f, 119.91f, 0.28f)
        cubicTo(128.24f, 1.75f, 136.12f, 4.33f, 142.87f, 9.52f)
        moveTo(175.29f, 83.8f)
        cubicTo(189.58f, 89.87f, 202.3f, 99.44f, 217.95f, 102.13f)
        cubicTo(223.91f, 103.16f, 228.3f, 101.81f, 233.36f, 99.11f)
        cubicTo(235.28f, 100.68f, 233.03f, 102.13f, 232.58f, 103.49f)
        cubicTo(225.26f, 110.81f, 212.43f, 114.18f, 202.64f, 110.25f)
        cubicTo(181.7f, 103.49f, 163.58f, 85.6f, 139.61f, 90.21f)
        cubicTo(139.16f, 90.89f, 138.48f, 91.22f, 137.81f, 91.33f)
        cubicTo(135.11f, 86.04f, 141.97f, 85.38f, 144.22f, 82.45f)
        cubicTo(154.12f, 77.6f, 165.95f, 78.39f, 175.29f, 83.8f)
        moveTo(150.42f, 135.58f)
        cubicTo(153f, 145.36f, 153.9f, 157.74f, 150.75f, 167.76f)
        cubicTo(141.19f, 192.52f, 124.08f, 215.49f, 123.17f, 243.39f)
        lineTo(124.31f, 251.5f)
        cubicTo(124.64f, 252.52f, 125.76f, 253.08f, 125.31f, 254.42f)
        cubicTo(124.86f, 255.44f, 123.62f, 255.44f, 122.95f, 254.99f)
        cubicTo(114.62f, 241.93f, 116.09f, 225.27f, 117.55f, 209.86f)
        cubicTo(119.69f, 195.33f, 129.14f, 183.85f, 135.89f, 171.25f)
        cubicTo(142.19f, 158.88f, 147.6f, 144.24f, 144.56f, 129.94f)
        cubicTo(144.56f, 128.59f, 146.82f, 129.05f, 147.48f, 129.72f)
        lineTo(150.42f, 135.58f)
    }
}
