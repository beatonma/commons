package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.VectorPath
import org.beatonma.commons.svg.plotPath
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.color.PoliticalColor

private const val RECTANGLE_WIDTH = 380f / 7F

class LabourCoopLogo : VectorGraphic(
    pathCount = 7,
    width = 380,
    height = 357,
    primaryColor = PoliticalColor.Party.Primary.LabourCoop,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getRosePath(),
            color = Color(0xff711f8f),
        )
        paths[1] = getRectPath(Color(0xffeb3000), 0) // red
        paths[2] = getRectPath(Color(0xfff9a519), 1) // orange
        paths[3] = getRectPath(Color(0xffffe200), 2) // yellow
        paths[4] = getRectPath(Color(0xff7ae020), 3) // green
        paths[5] = getRectPath(Color(0xff10c2ff), 4) // cyan
        paths[6] = getRectPath(Color(0xff282bbf), 5) // blue
    }

    private fun getRosePath(): Path = Path().apply {
        moveTo(268f, 329.5f)
        cubicTo(253f, 329.5f, 229f, 323.5f, 223f, 320.5f)
        cubicTo(213f, 315.5f, 212f, 309.5f, 212f, 300.5f)
        cubicTo(212f, 292.5f, 215f, 282.5f, 223f, 277.5f)
        cubicTo(231f, 274.5f, 238f, 271.5f, 243f, 268.5f)
        cubicTo(249f, 260.5f, 262f, 257.5f, 279f, 257.5f)
        cubicTo(288f, 257.5f, 294f, 254.5f, 302f, 257.5f)
        cubicTo(316f, 257.5f, 331f, 260.5f, 342f, 265.5f)
        cubicTo(348f, 268.5f, 353f, 271.5f, 360f, 274.5f)
        cubicTo(363f, 277.5f, 380f, 282.5f, 380f, 288.5f)
        cubicTo(368f, 283.5f, 357f, 280.5f, 345f, 280.5f)
        cubicTo(334f, 277.5f, 328f, 280.5f, 328f, 282.5f)
        cubicTo(325f, 288.5f, 322f, 288.5f, 319f, 288.5f)
        cubicTo(316f, 288.5f, 314f, 298.5f, 302f, 312.5f)
        cubicTo(291f, 329.5f, 279f, 329.5f, 268f, 329.5f)
        close()

        moveTo(114f, 312.5f)
        cubicTo(113f, 309.5f, 111f, 309.5f, 110f, 303.5f)
        cubicTo(109f, 300.5f, 108f, 295.5f, 108f, 282.5f)
        cubicTo(108f, 277.5f, 108f, 277.5f, 109f, 271.5f)
        cubicTo(110f, 265.5f, 112f, 257.5f, 113f, 257.5f)
        cubicTo(114f, 254.5f, 113f, 251.5f, 113f, 248.5f)
        cubicTo(111f, 240.5f, 114f, 227.5f, 120f, 222.5f)
        cubicTo(124f, 217.5f, 128f, 215.5f, 130f, 216.5f)
        cubicTo(133f, 218.5f, 136f, 223.5f, 139f, 229.5f)
        lineTo(140f, 230.5f)
        lineTo(137f, 230.5f)
        cubicTo(134f, 230.5f, 134f, 230.5f, 135f, 234.5f)
        cubicTo(137f, 234.5f, 138f, 237.5f, 138f, 240.5f)
        cubicTo(138f, 240.5f, 138f, 243.5f, 133f, 248.5f)
        cubicTo(128f, 257.5f, 126f, 260.5f, 125f, 262.5f)
        lineTo(128f, 262.5f)
        cubicTo(130f, 262.5f, 130f, 262.5f, 133f, 260.5f)
        cubicTo(138f, 254.5f, 140f, 251.5f, 141f, 251.5f)
        cubicTo(142f, 254.5f, 140f, 257.5f, 138f, 260.5f)
        cubicTo(136f, 262.5f, 134f, 265.5f, 131f, 268.5f)
        lineTo(128f, 271.5f)
        lineTo(129f, 271.5f)
        cubicTo(131f, 274.5f, 129f, 277.5f, 124f, 282.5f)
        lineTo(122f, 292.5f)
        lineTo(120f, 300.5f)
        cubicTo(120f, 306.5f, 119f, 312.5f, 119f, 312.5f)
        lineTo(117f, 312.5f)
        close()

        moveTo(181f, 262.5f)
        cubicTo(172f, 260.5f, 166f, 257.5f, 161f, 254.5f)
        cubicTo(155f, 248.5f, 152f, 240.5f, 143f, 212.5f)
        cubicTo(139f, 201.5f, 138f, 196.5f, 137f, 195.5f)
        cubicTo(135f, 192.5f, 135f, 190.5f, 135f, 189.5f)
        lineTo(136f, 188.5f)
        lineTo(139f, 189.5f)
        cubicTo(143f, 190.5f, 151f, 192.5f, 154f, 194.5f)
        cubicTo(159f, 196.5f, 163f, 199.5f, 169f, 203.5f)
        cubicTo(174f, 205.5f, 177f, 208.5f, 180f, 211.5f)
        cubicTo(181f, 214.5f, 183f, 219.5f, 185f, 224.5f)
        cubicTo(186f, 226.5f, 189f, 229.5f, 193f, 231.5f)
        cubicTo(195f, 231.5f, 198f, 234.5f, 199f, 234.5f)
        cubicTo(201f, 237.5f, 202f, 237.5f, 206f, 237.5f)
        cubicTo(223f, 243.5f, 238f, 248.5f, 255f, 248.5f)
        lineTo(255f, 251.5f)
        cubicTo(265f, 251.5f, 232f, 257.5f, 223f, 257.5f)
        cubicTo(218f, 257.5f, 208f, 257.5f, 202f, 260.5f)
        cubicTo(192f, 262.5f, 186f, 262.5f, 181f, 262.5f)
        close()

        moveTo(106f, 248.5f)
        cubicTo(105f, 245.5f, 99f, 243.5f, 98f, 240.5f)
        lineTo(94f, 240.5f)
        cubicTo(93f, 240.5f, 93f, 243.5f, 92f, 243.5f)
        cubicTo(90f, 248.5f, 88.4f, 248.5f, 83.6f, 248.5f)
        cubicTo(79.8f, 251.5f, 69.4f, 248.5f, 62.7f, 245.5f)
        cubicTo(54.2f, 243.5f, 47.5f, 240.5f, 40.9f, 230.5f)
        cubicTo(34.2f, 224.5f, 22.8f, 205.5f, 15.2f, 192.5f)
        cubicTo(9.5f, 181.5f, 6.7f, 174.5f, 2.9f, 162.5f)
        cubicTo(0f, 153.5f, 0f, 150.5f, 0f, 147.5f)
        cubicTo(0f, 143.5f, 0f, 142.5f, 1.9f, 141.5f)
        cubicTo(4.8f, 140.5f, 21.9f, 142.5f, 35.2f, 147.5f)
        cubicTo(46.6f, 150.5f, 58.9f, 155.5f, 60.8f, 157.5f)
        cubicTo(63.7f, 159.5f, 69.4f, 164.5f, 72.2f, 167.5f)
        cubicTo(81.7f, 177.5f, 88.4f, 188.5f, 91f, 200.5f)
        cubicTo(94f, 207.5f, 95f, 213.5f, 95f, 223.5f)
        cubicTo(95f, 230.5f, 95f, 230.5f, 99f, 234.5f)
        cubicTo(105f, 243.5f, 108f, 245.5f, 109f, 251.5f)
        lineTo(108f, 251.5f)
        cubicTo(108f, 254.5f, 107f, 251.5f, 106f, 248.5f)
        close()

        moveTo(232f, 237.5f)
        cubicTo(223f, 234.5f, 220f, 234.5f, 215f, 229.5f)
        cubicTo(212f, 228.5f, 208f, 226.5f, 204f, 224.5f)
        cubicTo(199f, 222.5f, 196f, 220.5f, 190f, 217.5f)
        cubicTo(186f, 214.5f, 185f, 212.5f, 183f, 209.5f)
        cubicTo(182f, 206.5f, 180f, 205.5f, 176f, 201.5f)
        cubicTo(175f, 200.5f, 175f, 199.5f, 175f, 199.5f)
        lineTo(175f, 198.5f)
        cubicTo(176f, 199.5f, 176f, 199.5f, 178f, 199.5f)
        cubicTo(183f, 201.5f, 195f, 203.5f, 212f, 205.5f)
        cubicTo(229f, 207.5f, 232f, 207.5f, 246f, 208.5f)
        cubicTo(265f, 209.5f, 259f, 209.5f, 262f, 208.5f)
        lineTo(262f, 205.5f)
        cubicTo(259f, 205.5f, 252f, 202.5f, 243f, 202.5f)
        cubicTo(240f, 201.5f, 235f, 201.5f, 235f, 201.5f)
        cubicTo(232f, 201.5f, 229f, 198.5f, 220f, 186.5f)
        cubicTo(215f, 176.5f, 212f, 177.5f, 212f, 177.5f)
        cubicTo(209f, 176.5f, 206f, 173.5f, 201f, 168.5f)
        cubicTo(196f, 165.5f, 196f, 166.5f, 197f, 162.5f)
        cubicTo(197f, 158.5f, 197f, 157.5f, 196f, 157.5f)
        cubicTo(195f, 157.5f, 193f, 157.5f, 189f, 159.5f)
        cubicTo(185f, 161.5f, 184f, 161.5f, 182f, 161.5f)
        cubicTo(182f, 161.5f, 180f, 161.5f, 180f, 160.5f)
        cubicTo(179f, 160.5f, 176f, 158.5f, 173f, 154.5f)
        lineTo(167f, 148.5f)
        cubicTo(166f, 150.5f, 166f, 150.5f, 166f, 151.5f)
        cubicTo(165f, 153.5f, 164f, 154.5f, 165f, 154.5f)
        cubicTo(165f, 154.5f, 173f, 160.5f, 182f, 167.5f)
        cubicTo(199f, 181.5f, 201f, 183.5f, 201f, 186.5f)
        cubicTo(202f, 186.5f, 201f, 187.5f, 201f, 188.5f)
        cubicTo(200f, 188.5f, 199f, 189.5f, 198f, 191.5f)
        cubicTo(195f, 194.5f, 193f, 195.5f, 192f, 195.5f)
        cubicTo(189f, 195.5f, 177f, 192.5f, 166f, 190.5f)
        cubicTo(154f, 186.5f, 150f, 185.5f, 138f, 173.5f)
        cubicTo(132f, 168.5f, 129f, 166.5f, 126f, 160.5f)
        cubicTo(124f, 153.5f, 121f, 146.5f, 120f, 138.5f)
        cubicTo(119f, 131.5f, 119f, 129.5f, 119f, 126.5f)
        cubicTo(121f, 118.5f, 121f, 114.5f, 120f, 109.5f)
        cubicTo(119f, 106.5f, 119f, 103.5f, 119f, 101.5f)
        cubicTo(120f, 100.5f, 121f, 100.5f, 123f, 100.5f)
        cubicTo(124f, 101.5f, 126f, 104.5f, 129f, 109.5f)
        cubicTo(135f, 117.5f, 139f, 122.5f, 143f, 126.5f)
        cubicTo(151f, 132.5f, 160f, 137.5f, 164f, 135.5f)
        cubicTo(167f, 133.5f, 167f, 131.5f, 163f, 124.5f)
        cubicTo(162f, 122.5f, 161f, 119.5f, 160f, 118.5f)
        cubicTo(158f, 114.5f, 157f, 111.5f, 152f, 106.5f)
        cubicTo(147f, 100.5f, 146f, 98.5f, 145f, 95.5f)
        cubicTo(144f, 91.5f, 144f, 88.5f, 145f, 85.5f)
        cubicTo(145f, 84.5f, 147f, 80.5f, 149f, 75f)
        cubicTo(156f, 62f, 157f, 60f, 158f, 52f)
        cubicTo(159f, 44f, 160f, 41f, 163f, 36f)
        lineTo(165f, 33f)
        lineTo(195f, 18f)
        cubicTo(212f, 10f, 226f, 2f, 226f, 2f)
        cubicTo(232f, 0f, 235f, 0f, 243f, 0f)
        cubicTo(255f, 0f, 262f, 1f, 271f, 4f)
        cubicTo(277f, 7f, 294f, 14f, 308f, 24f)
        cubicTo(322f, 33f, 322f, 33f, 325f, 38f)
        cubicTo(339f, 49f, 345f, 61f, 348f, 71f)
        cubicTo(351f, 82.5f, 345f, 90.5f, 331f, 95.5f)
        cubicTo(331f, 97.5f, 325f, 99.5f, 325f, 99.5f)
        cubicTo(325f, 99.5f, 322f, 95.5f, 322f, 93.5f)
        cubicTo(319f, 88.5f, 322f, 84.5f, 325f, 78f)
        cubicTo(328f, 73f, 328f, 72f, 322f, 68f)
        cubicTo(316f, 66f, 316f, 65f, 314f, 61f)
        cubicTo(314f, 55f, 314f, 53f, 311f, 52f)
        cubicTo(311f, 52f, 308f, 52f, 308f, 51f)
        cubicTo(305f, 51f, 305f, 51f, 302f, 50f)
        cubicTo(302f, 49f, 299f, 49f, 299f, 49f)
        lineTo(296f, 49f)
        lineTo(296f, 51f)
        cubicTo(288f, 48f, 288f, 48f, 285f, 45f)
        cubicTo(279f, 40f, 277f, 38f, 274f, 35f)
        lineTo(274f, 33f)
        lineTo(262f, 33f)
        lineTo(262f, 34f)
        cubicTo(262f, 35f, 265f, 36f, 265f, 38f)
        lineTo(265f, 41f)
        cubicTo(265f, 43f, 262f, 46f, 259f, 47f)
        cubicTo(259f, 48f, 252f, 48f, 249f, 46f)
        lineTo(243f, 46f)
        cubicTo(243f, 47f, 240f, 48f, 238f, 48f)
        cubicTo(226f, 49f, 220f, 49f, 218f, 51f)
        cubicTo(215f, 52f, 212f, 56f, 208f, 62f)
        cubicTo(206f, 65f, 206f, 66f, 206f, 67f)
        cubicTo(206f, 68f, 206f, 71f, 205f, 71f)
        cubicTo(204f, 73f, 201f, 78f, 200f, 80.5f)
        cubicTo(195f, 84.5f, 186f, 87.5f, 179f, 87.5f)
        cubicTo(176f, 87.5f, 175f, 88.5f, 175f, 89.5f)
        lineTo(175f, 98.5f)
        cubicTo(175f, 109.5f, 176f, 111.5f, 176f, 113.5f)
        cubicTo(177f, 116.5f, 177f, 117.5f, 179f, 117.5f)
        cubicTo(180f, 118.5f, 183f, 119.5f, 184f, 119.5f)
        cubicTo(186f, 119.5f, 188f, 118.5f, 189f, 116.5f)
        cubicTo(190f, 114.5f, 193f, 110.5f, 193f, 109.5f)
        lineTo(189f, 109.5f)
        cubicTo(182f, 108.5f, 180f, 106.5f, 180f, 104.5f)
        cubicTo(180f, 103.5f, 182f, 100.5f, 185f, 98.5f)
        cubicTo(193f, 92.5f, 201f, 89.5f, 208f, 87.5f)
        cubicTo(212f, 86.5f, 215f, 86.5f, 215f, 90.5f)
        lineTo(218f, 92.5f)
        lineTo(223f, 92.5f)
        cubicTo(229f, 91.5f, 235f, 91.5f, 238f, 92.5f)
        cubicTo(240f, 93.5f, 243f, 92.5f, 243f, 90.5f)
        cubicTo(243f, 90.5f, 243f, 89.5f, 240f, 88.5f)
        cubicTo(238f, 86.5f, 232f, 85.5f, 223f, 85.5f)
        cubicTo(218f, 85.5f, 218f, 85.5f, 215f, 82.5f)
        cubicTo(212f, 80.5f, 212f, 77f, 212f, 74f)
        lineTo(212f, 71f)
        cubicTo(215f, 64f, 229f, 57f, 240f, 55f)
        cubicTo(252f, 53f, 274f, 53f, 282f, 55f)
        cubicTo(296f, 57f, 302f, 63f, 308f, 73f)
        cubicTo(311f, 83.5f, 311f, 90.5f, 308f, 95.5f)
        cubicTo(308f, 96.5f, 305f, 98.5f, 302f, 98.5f)
        cubicTo(299f, 100.5f, 299f, 101.5f, 296f, 104.5f)
        cubicTo(294f, 107.5f, 294f, 109.5f, 285f, 111.5f)
        cubicTo(279f, 116.5f, 279f, 117.5f, 277f, 119.5f)
        lineTo(277f, 122.5f)
        lineTo(274f, 123.5f)
        cubicTo(271f, 122.5f, 271f, 120.5f, 265f, 111.5f)
        cubicTo(265f, 109.5f, 262f, 106.5f, 262f, 105.5f)
        cubicTo(262f, 103.5f, 259f, 101.5f, 259f, 100.5f)
        cubicTo(255f, 96.5f, 255f, 94.5f, 252f, 94.5f)
        cubicTo(249f, 94.5f, 249f, 95.5f, 249f, 99.5f)
        cubicTo(249f, 102.5f, 249f, 103.5f, 246f, 106.5f)
        cubicTo(246f, 108.5f, 240f, 110.5f, 240f, 110.5f)
        cubicTo(238f, 111.5f, 232f, 110.5f, 226f, 108.5f)
        cubicTo(223f, 107.5f, 220f, 106.5f, 218f, 105.5f)
        lineTo(212f, 101.5f)
        cubicTo(212f, 100.5f, 208f, 99.5f, 208f, 99.5f)
        lineTo(205f, 102.5f)
        cubicTo(204f, 103.5f, 204f, 104.5f, 205f, 106.5f)
        cubicTo(206f, 108.5f, 208f, 109.5f, 215f, 111.5f)
        lineTo(220f, 113.5f)
        lineTo(226f, 117.5f)
        cubicTo(232f, 123.5f, 232f, 123.5f, 235f, 121.5f)
        cubicTo(243f, 116.5f, 255f, 113.5f, 262f, 115.5f)
        cubicTo(262f, 116.5f, 265f, 117.5f, 265f, 119.5f)
        cubicTo(265f, 121.5f, 265f, 125.5f, 262f, 127.5f)
        cubicTo(259f, 138.5f, 249f, 145.5f, 232f, 147.5f)
        cubicTo(223f, 148.5f, 215f, 148.5f, 206f, 145.5f)
        lineTo(204f, 145.5f)
        cubicTo(203f, 145.5f, 203f, 146.5f, 205f, 147.5f)
        cubicTo(218f, 155.5f, 220f, 157.5f, 223f, 157.5f)
        lineTo(229f, 155.5f)
        cubicTo(232f, 154.5f, 238f, 153.5f, 238f, 153.5f)
        cubicTo(243f, 151.5f, 243f, 152.5f, 249f, 149.5f)
        cubicTo(259f, 144.5f, 274f, 134.5f, 279f, 126.5f)
        cubicTo(282f, 119.5f, 282f, 116.5f, 288f, 120.5f)
        cubicTo(294f, 120.5f, 291f, 118.5f, 294f, 112.5f)
        cubicTo(296f, 106.5f, 299f, 105.5f, 299f, 108.5f)
        cubicTo(299f, 113.5f, 302f, 140.5f, 294f, 150.5f)
        cubicTo(288f, 154.5f, 282f, 155.5f, 277f, 158.5f)
        cubicTo(271f, 159.5f, 268f, 160.5f, 265f, 161.5f)
        cubicTo(262f, 161.5f, 259f, 162.5f, 254f, 160.5f)
        cubicTo(248f, 157.5f, 249f, 156.5f, 246f, 156.5f)
        cubicTo(243f, 156.5f, 240f, 156.5f, 240f, 157.5f)
        lineTo(240f, 162.5f)
        cubicTo(243f, 167.5f, 249f, 170.5f, 259f, 172.5f)
        cubicTo(262f, 173.5f, 274f, 173.5f, 277f, 172.5f)
        cubicTo(288f, 170.5f, 296f, 164.5f, 299f, 158.5f)
        cubicTo(299f, 147.5f, 302f, 135.5f, 308f, 137.5f)
        lineTo(314f, 137.5f)
        cubicTo(314f, 138.5f, 314f, 137.5f, 316f, 136.5f)
        cubicTo(316f, 134.5f, 319f, 132.5f, 322f, 131.5f)
        cubicTo(325f, 127.5f, 325f, 128.5f, 325f, 124.5f)
        lineTo(325f, 119.5f)
        cubicTo(325f, 115.5f, 328f, 113.5f, 334f, 114.5f)
        cubicTo(339f, 114.5f, 339f, 114.5f, 339f, 115.5f)
        cubicTo(345f, 117.5f, 348f, 122.5f, 351f, 130.5f)
        cubicTo(353f, 135.5f, 353f, 140.5f, 353f, 146.5f)
        cubicTo(353f, 153.5f, 353f, 163.5f, 351f, 169.5f)
        cubicTo(348f, 187.5f, 328f, 207.5f, 305f, 220.5f)
        cubicTo(296f, 224.5f, 285f, 228.5f, 274f, 231.5f)
        cubicTo(259f, 234.5f, 252f, 237.5f, 240f, 237.5f)
        close()

        moveTo(115f, 186.5f)
        cubicTo(113f, 186.5f, 109f, 184.5f, 106f, 183.5f)
        cubicTo(103f, 181.5f, 98f, 176.5f, 91f, 169.5f)
        cubicTo(84.6f, 163.5f, 81.7f, 155.5f, 77.9f, 145.5f)
        cubicTo(75.1f, 132.5f, 73.2f, 120.5f, 73.2f, 112.5f)
        cubicTo(75.1f, 99.5f, 81.7f, 83.5f, 90f, 71f)
        cubicTo(98f, 61f, 109f, 53f, 123f, 51f)
        cubicTo(129f, 49f, 135f, 49f, 137f, 50f)
        lineTo(137f, 52f)
        cubicTo(137f, 54f, 138f, 55f, 138f, 57f)
        lineTo(138f, 68f)
        cubicTo(135f, 77f, 129f, 86.5f, 119f, 95.5f)
        cubicTo(113f, 100.5f, 111f, 103.5f, 110f, 107.5f)
        lineTo(110f, 122.5f)
        cubicTo(111f, 128.5f, 116f, 148.5f, 117f, 154.5f)
        cubicTo(119f, 157.5f, 124f, 167.5f, 128f, 175.5f)
        cubicTo(128f, 176.5f, 129f, 176.5f, 129f, 177.5f)
        cubicTo(129f, 178.5f, 128f, 179.5f, 126f, 179.5f)
        cubicTo(124f, 179.5f, 122f, 180.5f, 121f, 182.5f)
        lineTo(121f, 184.5f)
        lineTo(120f, 186.5f)
        close()

        moveTo(6F * RECTANGLE_WIDTH, 343.5f)
        lineTo(6F * RECTANGLE_WIDTH, 356.5f)
        lineTo(380f, 356.5f)
        lineTo(380f, 343.5f)
        close()
    }

    private fun getRectPath(color: Color, position: Int): VectorPath {
        val xOffset = RECTANGLE_WIDTH * position
        return vectorPath(
            path = plotPath {
                moveTo(xOffset + 0f, 343.5f)
                lineTo(xOffset + 0f, 356.5f)
                lineTo(xOffset + RECTANGLE_WIDTH, 356.5f)
                lineTo(xOffset + RECTANGLE_WIDTH, 343.5f)
                close()
            },
            color = color
        )
    }
}
