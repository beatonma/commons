package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.drawscope.Fill
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.plotPath
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.color.PoliticalColor

class LabourLogo : VectorGraphic(
    pathCount = 1,
    width = 381,
    height = 330,
    primaryColor = PoliticalColor.Party.Primary.Labour
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath(),
            color = primaryColor,
            style = Fill,
        )
    }

    private fun getPath() = plotPath {
        moveTo(268.1f, 329.1f)
        cubicTo(253.1f, 329.1f, 229.1f, 323.1f, 223.1f, 320.1f)
        cubicTo(213.1f, 315.1f, 212.1f, 309.1f, 212.1f, 300.1f)
        cubicTo(212.1f, 292.1f, 215.1f, 282.1f, 223.1f, 277.1f)
        cubicTo(231.1f, 274.1f, 238.1f, 271.1f, 243.1f, 268.1f)
        cubicTo(249.1f, 260.1f, 262.1f, 257.1f, 279.1f, 257.1f)
        cubicTo(288.1f, 257.1f, 294.1f, 254.1f, 302.1f, 257.1f)
        cubicTo(316.1f, 257.1f, 331.1f, 260.1f, 342.1f, 265.1f)
        cubicTo(348.1f, 268.1f, 353.1f, 271.1f, 360.1f, 274.1f)
        cubicTo(363.1f, 277.1f, 380.1f, 282.1f, 380.1f, 288.1f)
        cubicTo(368.1f, 283.1f, 357.1f, 280.1f, 345.1f, 280.1f)
        cubicTo(334.1f, 277.1f, 328.1f, 280.1f, 328.1f, 282.1f)
        cubicTo(325.1f, 288.1f, 322.1f, 288.1f, 319.1f, 288.1f)
        cubicTo(316.1f, 288.1f, 314.1f, 298.1f, 302.1f, 312.1f)
        cubicTo(291.1f, 329.1f, 279.1f, 329.1f, 268.1f, 329.1f)
        close()

        moveTo(114.1f, 312.1f)
        cubicTo(113.1f, 309.1f, 111.1f, 309.1f, 110.1f, 303.1f)
        cubicTo(109.1f, 300.1f, 108.1f, 295.1f, 108.1f, 282.1f)
        cubicTo(108.1f, 277.1f, 108.1f, 277.1f, 109.1f, 271.1f)
        cubicTo(110.1f, 265.1f, 112.1f, 257.1f, 113.1f, 257.1f)
        cubicTo(114.1f, 254.1f, 113.1f, 251.1f, 113.1f, 248.1f)
        cubicTo(111.1f, 240.1f, 114.1f, 227.1f, 120.1f, 222.1f)
        cubicTo(124.1f, 217.1f, 128.1f, 215.1f, 130.1f, 216.1f)
        cubicTo(133.1f, 218.1f, 136.1f, 223.1f, 139.1f, 229.1f)
        lineTo(140.1f, 230.1f)
        lineTo(137.1f, 230.1f)
        cubicTo(134.1f, 230.1f, 134.1f, 230.1f, 135.1f, 234.1f)
        cubicTo(137.1f, 234.1f, 138.1f, 237.1f, 138.1f, 240.1f)
        cubicTo(138.1f, 240.1f, 138.1f, 243.1f, 133.1f, 248.1f)
        cubicTo(128.1f, 257.1f, 126.1f, 260.1f, 125.1f, 262.1f)
        lineTo(126.1f, 262.1f)
        lineTo(128.1f, 262.1f)
        cubicTo(128.1f, 262.1f, 130.1f, 262.1f, 133.1f, 260.1f)
        cubicTo(138.1f, 254.1f, 140.1f, 251.1f, 141.1f, 251.1f)
        cubicTo(142.1f, 254.1f, 140.1f, 257.1f, 138.1f, 260.1f)
        cubicTo(136.1f, 262.1f, 134.1f, 265.1f, 131.1f, 268.1f)
        lineTo(128.1f, 271.1f)
        lineTo(129.1f, 271.1f)
        cubicTo(131.1f, 274.1f, 129.1f, 277.1f, 124.1f, 282.1f)
        lineTo(122.1f, 292.1f)
        lineTo(120.1f, 300.1f)
        cubicTo(120.1f, 306.1f, 119.1f, 312.1f, 119.1f, 312.1f)
        lineTo(117.1f, 312.1f)
        close()

        moveTo(181.1f, 262.1f)
        cubicTo(172.1f, 260.1f, 166.1f, 257.1f, 161.1f, 254.1f)
        cubicTo(155.1f, 248.1f, 152.1f, 240.1f, 143.1f, 212.1f)
        cubicTo(139.1f, 201.1f, 138.1f, 196.1f, 137.1f, 195.1f)
        cubicTo(135.1f, 192.1f, 135.1f, 190.1f, 135.1f, 189.1f)
        lineTo(136.1f, 188.1f)
        lineTo(139.1f, 189.1f)
        cubicTo(143.1f, 190.1f, 151.1f, 192.1f, 154.1f, 194.1f)
        cubicTo(159.1f, 196.1f, 163.1f, 199.1f, 169.1f, 203.1f)
        cubicTo(174.1f, 205.1f, 177.1f, 208.1f, 180.1f, 211.1f)
        cubicTo(181.1f, 214.1f, 183.1f, 219.1f, 185.1f, 224.1f)
        cubicTo(186.1f, 226.1f, 189.1f, 229.1f, 193.1f, 231.1f)
        cubicTo(195.1f, 231.1f, 198.1f, 234.1f, 199.1f, 234.1f)
        cubicTo(201.1f, 237.1f, 202.1f, 237.1f, 206.1f, 237.1f)
        cubicTo(223.1f, 243.1f, 238.1f, 248.1f, 255.1f, 248.1f)
        lineTo(255.1f, 251.1f)
        cubicTo(265.1f, 251.1f, 232.1f, 257.1f, 223.1f, 257.1f)
        cubicTo(218.1f, 257.1f, 208.1f, 257.1f, 202.1f, 260.1f)
        cubicTo(192.1f, 262.1f, 186.1f, 262.1f, 181.1f, 262.1f)
        close()

        moveTo(106.1f, 248.1f)
        cubicTo(105.1f, 245.1f, 99.1f, 243.1f, 98.1f, 240.1f)
        lineTo(94.1f, 240.1f)
        cubicTo(93.1f, 240.1f, 93.1f, 243.1f, 92.1f, 243.1f)
        cubicTo(90.1f, 248.1f, 88.1f, 248.1f, 84.1f, 248.1f)
        cubicTo(79.8f, 251.1f, 69.4f, 248.1f, 62.7f, 245.1f)
        cubicTo(54.2f, 243.1f, 47.5f, 240.1f, 40.9f, 230.1f)
        cubicTo(34.2f, 224.1f, 22.8f, 205.1f, 15.2f, 192.1f)
        cubicTo(9.5f, 181.1f, 6.7f, 174.1f, 2.9f, 162.1f)
        cubicTo(0f, 153.1f, 0f, 150.1f, 0f, 147.1f)
        cubicTo(0f, 143.1f, 0f, 142.1f, 1.9f, 141.1f)
        cubicTo(4.8f, 140.1f, 21.9f, 142.1f, 35.2f, 147.1f)
        cubicTo(46.6f, 150.1f, 58.9f, 155.1f, 60.8f, 157.1f)
        cubicTo(63.7f, 159.1f, 69.4f, 164.1f, 72.2f, 167.1f)
        cubicTo(81.7f, 177.1f, 88.1f, 188.1f, 91.1f, 200.1f)
        cubicTo(94.1f, 207.1f, 95.1f, 213.1f, 95.1f, 223.1f)
        cubicTo(95.1f, 230.1f, 95.1f, 230.1f, 99.1f, 234.1f)
        cubicTo(105.1f, 243.1f, 108.1f, 245.1f, 109.1f, 251.1f)
        lineTo(108.1f, 251.1f)
        cubicTo(108.1f, 254.1f, 107.1f, 251.1f, 106.1f, 248.1f)
        close()

        moveTo(232.1f, 237.1f)
        cubicTo(223.1f, 234.1f, 220.1f, 234.1f, 215.1f, 229.1f)
        cubicTo(212.1f, 228.1f, 208.1f, 226.1f, 204.1f, 224.1f)
        cubicTo(199.1f, 222.1f, 196.1f, 220.1f, 190.1f, 217.1f)
        cubicTo(186.1f, 214.1f, 185.1f, 212.1f, 183.1f, 209.1f)
        cubicTo(182.1f, 206.1f, 180.1f, 205.1f, 176.1f, 201.1f)
        cubicTo(175.1f, 200.1f, 175.1f, 199.1f, 175.1f, 199.1f)
        lineTo(175.1f, 198.1f)
        cubicTo(175.1f, 198.1f, 176.1f, 199.1f, 178.1f, 199.1f)
        cubicTo(183.1f, 201.1f, 195.1f, 203.1f, 212.1f, 205.1f)
        cubicTo(229.1f, 207.1f, 232.1f, 207.1f, 246.1f, 208.1f)
        cubicTo(265.1f, 209.1f, 259.1f, 209.1f, 262.1f, 208.1f)
        lineTo(262.1f, 205.1f)
        cubicTo(259.1f, 205.1f, 252.1f, 202.1f, 243.1f, 202.1f)
        cubicTo(240.1f, 201.1f, 235.1f, 201.1f, 235.1f, 201.1f)
        cubicTo(232.1f, 201.1f, 229.1f, 198.1f, 220.1f, 186.1f)
        cubicTo(215.1f, 176.1f, 212.1f, 177.1f, 212.1f, 177.1f)
        cubicTo(209.1f, 176.1f, 206.1f, 173.1f, 201.1f, 168.1f)
        cubicTo(196.1f, 165.1f, 196.1f, 166.1f, 197.1f, 162.1f)
        cubicTo(197.1f, 158.1f, 197.1f, 157.1f, 196.1f, 157.1f)
        cubicTo(195.1f, 157.1f, 193.1f, 157.1f, 189.1f, 159.1f)
        cubicTo(185.1f, 161.1f, 184.1f, 161.1f, 182.1f, 161.1f)
        cubicTo(182.1f, 161.1f, 180.1f, 161.1f, 180.1f, 160.1f)
        cubicTo(179.1f, 160.1f, 176.1f, 158.1f, 173.1f, 154.1f)
        lineTo(167.1f, 148.1f)
        cubicTo(167.1f, 148.1f, 166.1f, 150.1f, 166.1f, 151.1f)
        cubicTo(165.1f, 153.1f, 164.1f, 154.1f, 165.1f, 154.1f)
        cubicTo(165.1f, 154.1f, 173.1f, 160.1f, 182.1f, 167.1f)
        cubicTo(199.1f, 181.1f, 201.1f, 183.1f, 201.1f, 186.1f)
        cubicTo(202.1f, 186.1f, 201.1f, 187.1f, 201.1f, 188.1f)
        cubicTo(200.1f, 188.1f, 199.1f, 189.1f, 198.1f, 191.1f)
        cubicTo(195.1f, 194.1f, 193.1f, 195.1f, 192.1f, 195.1f)
        cubicTo(189.1f, 195.1f, 177.1f, 192.1f, 166.1f, 190.1f)
        cubicTo(154.1f, 186.1f, 150.1f, 185.1f, 138.1f, 173.1f)
        cubicTo(132.1f, 168.1f, 129.1f, 166.1f, 126.1f, 160.1f)
        cubicTo(124.1f, 153.1f, 121.1f, 146.1f, 120.1f, 138.1f)
        cubicTo(119.1f, 131.1f, 119.1f, 129.1f, 119.1f, 126.1f)
        cubicTo(121.1f, 118.1f, 121.1f, 114.1f, 120.1f, 109.1f)
        cubicTo(119.1f, 106.1f, 119.1f, 103.1f, 119.1f, 101.1f)
        cubicTo(120.1f, 100.1f, 121.1f, 100.1f, 123.1f, 100.1f)
        cubicTo(124.1f, 101.1f, 126.1f, 104.1f, 129.1f, 109.1f)
        cubicTo(135.1f, 117.1f, 139.1f, 122.1f, 143.1f, 126.1f)
        cubicTo(151.1f, 132.1f, 160.1f, 137.1f, 164.1f, 135.1f)
        cubicTo(167.1f, 133.1f, 167.1f, 131.1f, 163.1f, 124.1f)
        cubicTo(162.1f, 122.1f, 161.1f, 119.1f, 160.1f, 118.1f)
        cubicTo(158.1f, 114.1f, 157.1f, 111.1f, 152.1f, 106.1f)
        cubicTo(147.1f, 100.1f, 146.1f, 98.1f, 145.1f, 95.1f)
        cubicTo(144.1f, 91.1f, 144.1f, 88.1f, 145.1f, 85.1f)
        cubicTo(145.1f, 84.1f, 147.1f, 80.1f, 149.1f, 75.1f)
        cubicTo(156.1f, 62.3f, 157.1f, 60.3f, 158.1f, 51.4f)
        cubicTo(159.1f, 43.8f, 160.1f, 40.9f, 163.1f, 36.2f)
        lineTo(165.1f, 33.3f)
        lineTo(195.1f, 18.1f)
        cubicTo(212.1f, 9.5f, 226.1f, 1.9f, 226.1f, 1.9f)
        cubicTo(232.1f, 0f, 235.1f, 0f, 243.1f, 0f)
        cubicTo(255.1f, 0f, 262.1f, 1f, 271.1f, 3.8f)
        cubicTo(277.1f, 6.7f, 294.1f, 14.3f, 308.1f, 23.8f)
        cubicTo(322.1f, 33.3f, 322.1f, 33.3f, 325.1f, 38.1f)
        cubicTo(339.1f, 48.5f, 345.1f, 61.3f, 348.1f, 71.2f)
        cubicTo(351.1f, 82.1f, 345.1f, 90.1f, 331.1f, 95.1f)
        cubicTo(331.1f, 97.1f, 325.1f, 99.1f, 325.1f, 99.1f)
        cubicTo(325.1f, 99.1f, 322.1f, 95.1f, 322.1f, 93.1f)
        cubicTo(319.1f, 88.1f, 322.1f, 84.1f, 325.1f, 78.1f)
        cubicTo(328.1f, 73.1f, 328.1f, 72.1f, 322.1f, 68.2f)
        cubicTo(316.1f, 66.2f, 316.1f, 65.2f, 314.1f, 61.3f)
        cubicTo(314.1f, 55.2f, 314.1f, 53.3f, 311.1f, 52.3f)
        cubicTo(311.1f, 51.4f, 308.1f, 51.4f, 308.1f, 50.4f)
        cubicTo(305.1f, 50.4f, 305.1f, 50.4f, 302.1f, 49.5f)
        cubicTo(302.1f, 48.5f, 299.1f, 48.5f, 299.1f, 48.5f)
        lineTo(296.1f, 48.5f)
        lineTo(296.1f, 49.5f)
        lineTo(296.1f, 50.4f)
        cubicTo(296.1f, 50.4f, 288.1f, 47.6f, 285.1f, 44.7f)
        cubicTo(279.1f, 40f, 277.1f, 38.1f, 274.1f, 35.2f)
        lineTo(274.1f, 33.3f)
        lineTo(268.1f, 33.3f)
        lineTo(262.1f, 33.3f)
        lineTo(262.1f, 34.3f)
        cubicTo(262.1f, 35.2f, 265.1f, 36.2f, 265.1f, 38.1f)
        lineTo(265.1f, 40.9f)
        cubicTo(265.1f, 42.8f, 262.1f, 45.7f, 259.1f, 46.6f)
        cubicTo(259.1f, 47.6f, 252.1f, 47.6f, 249.1f, 45.7f)
        lineTo(246.1f, 45.7f)
        lineTo(243.1f, 45.7f)
        cubicTo(243.1f, 46.6f, 240.1f, 47.6f, 238.1f, 47.6f)
        cubicTo(226.1f, 48.5f, 220.1f, 48.5f, 218.1f, 50.4f)
        cubicTo(215.1f, 52.3f, 212.1f, 56.1f, 208.1f, 62.3f)
        cubicTo(206.1f, 65.2f, 206.1f, 66.2f, 206.1f, 67.2f)
        cubicTo(206.1f, 68.2f, 206.1f, 71.2f, 205.1f, 71.2f)
        cubicTo(204.1f, 73.1f, 201.1f, 78.1f, 200.1f, 80.1f)
        cubicTo(195.1f, 84.1f, 186.1f, 87.1f, 179.1f, 87.1f)
        cubicTo(176.1f, 87.1f, 175.1f, 88.1f, 175.1f, 89.1f)
        lineTo(175.1f, 98.1f)
        cubicTo(175.1f, 109.1f, 176.1f, 111.1f, 176.1f, 113.1f)
        cubicTo(177.1f, 116.1f, 177.1f, 117.1f, 179.1f, 117.1f)
        cubicTo(180.1f, 118.1f, 183.1f, 119.1f, 184.1f, 119.1f)
        cubicTo(186.1f, 119.1f, 188.1f, 118.1f, 189.1f, 116.1f)
        cubicTo(190.1f, 114.1f, 193.1f, 110.1f, 193.1f, 109.1f)
        lineTo(189.1f, 109.1f)
        cubicTo(182.1f, 108.1f, 180.1f, 106.1f, 180.1f, 104.1f)
        cubicTo(180.1f, 103.1f, 182.1f, 100.1f, 185.1f, 98.1f)
        cubicTo(193.1f, 92.1f, 201.1f, 89.1f, 208.1f, 87.1f)
        cubicTo(212.1f, 86.1f, 215.1f, 86.1f, 215.1f, 90.1f)
        lineTo(218.1f, 92.1f)
        lineTo(223.1f, 92.1f)
        cubicTo(229.1f, 91.1f, 235.1f, 91.1f, 238.1f, 92.1f)
        cubicTo(240.1f, 93.1f, 243.1f, 92.1f, 243.1f, 90.1f)
        cubicTo(243.1f, 90.1f, 243.1f, 89.1f, 240.1f, 88.1f)
        cubicTo(238.1f, 86.1f, 232.1f, 85.1f, 223.1f, 85.1f)
        cubicTo(218.1f, 85.1f, 218.1f, 85.1f, 215.1f, 82.1f)
        cubicTo(212.1f, 80.1f, 212.1f, 77.1f, 212.1f, 74.1f)
        lineTo(212.1f, 71.2f)
        cubicTo(215.1f, 64.3f, 229.1f, 57.3f, 240.1f, 55.2f)
        cubicTo(252.1f, 53.3f, 274.1f, 53.3f, 282.1f, 55.2f)
        cubicTo(296.1f, 57.3f, 302.1f, 63.3f, 308.1f, 73.1f)
        cubicTo(311.1f, 83.1f, 311.1f, 90.1f, 308.1f, 95.1f)
        cubicTo(308.1f, 96.1f, 305.1f, 98.1f, 302.1f, 98.1f)
        cubicTo(299.1f, 100.1f, 299.1f, 101.1f, 296.1f, 104.1f)
        cubicTo(294.1f, 107.1f, 294.1f, 109.1f, 285.1f, 111.1f)
        cubicTo(279.1f, 116.1f, 279.1f, 117.1f, 277.1f, 119.1f)
        lineTo(277.1f, 122.1f)
        lineTo(274.1f, 123.1f)
        cubicTo(271.1f, 122.1f, 271.1f, 120.1f, 265.1f, 111.1f)
        cubicTo(265.1f, 109.1f, 262.1f, 106.1f, 262.1f, 105.1f)
        cubicTo(262.1f, 103.1f, 259.1f, 101.1f, 259.1f, 100.1f)
        cubicTo(255.1f, 96.1f, 255.1f, 94.1f, 252.1f, 94.1f)
        cubicTo(249.1f, 94.1f, 249.1f, 95.1f, 249.1f, 99.1f)
        cubicTo(249.1f, 102.1f, 249.1f, 103.1f, 246.1f, 106.1f)
        cubicTo(246.1f, 108.1f, 240.1f, 110.1f, 240.1f, 110.1f)
        cubicTo(238.1f, 111.1f, 232.1f, 110.1f, 226.1f, 108.1f)
        cubicTo(223.1f, 107.1f, 220.1f, 106.1f, 218.1f, 105.1f)
        lineTo(212.1f, 101.1f)
        cubicTo(212.1f, 100.1f, 208.1f, 99.1f, 208.1f, 99.1f)
        lineTo(205.1f, 102.1f)
        cubicTo(204.1f, 103.1f, 204.1f, 104.1f, 205.1f, 106.1f)
        cubicTo(206.1f, 108.1f, 208.1f, 109.1f, 215.1f, 111.1f)
        lineTo(220.1f, 113.1f)
        lineTo(226.1f, 117.1f)
        cubicTo(232.1f, 123.1f, 232.1f, 123.1f, 235.1f, 121.1f)
        cubicTo(243.1f, 116.1f, 255.1f, 113.1f, 262.1f, 115.1f)
        cubicTo(262.1f, 116.1f, 265.1f, 117.1f, 265.1f, 119.1f)
        cubicTo(265.1f, 121.1f, 265.1f, 125.1f, 262.1f, 127.1f)
        cubicTo(259.1f, 138.1f, 249.1f, 145.1f, 232.1f, 147.1f)
        cubicTo(223.1f, 148.1f, 215.1f, 148.1f, 206.1f, 145.1f)
        lineTo(204.1f, 145.1f)
        cubicTo(203.1f, 145.1f, 203.1f, 146.1f, 205.1f, 147.1f)
        cubicTo(218.1f, 155.1f, 220.1f, 157.1f, 223.1f, 157.1f)
        lineTo(229.1f, 155.1f)
        cubicTo(232.1f, 154.1f, 238.1f, 153.1f, 238.1f, 153.1f)
        cubicTo(243.1f, 151.1f, 243.1f, 152.1f, 249.1f, 149.1f)
        cubicTo(259.1f, 144.1f, 274.1f, 134.1f, 279.1f, 126.1f)
        cubicTo(282.1f, 119.1f, 282.1f, 116.1f, 288.1f, 120.1f)
        cubicTo(294.1f, 120.1f, 291.1f, 118.1f, 294.1f, 112.1f)
        cubicTo(296.1f, 106.1f, 299.1f, 105.1f, 299.1f, 108.1f)
        cubicTo(299.1f, 113.1f, 302.1f, 140.1f, 294.1f, 150.1f)
        cubicTo(288.1f, 154.1f, 282.1f, 155.1f, 277.1f, 158.1f)
        cubicTo(271.1f, 159.1f, 268.1f, 160.1f, 265.1f, 161.1f)
        cubicTo(262.1f, 161.1f, 259.1f, 162.1f, 254.1f, 160.1f)
        cubicTo(248.1f, 157.1f, 249.1f, 156.1f, 246.1f, 156.1f)
        cubicTo(243.1f, 156.1f, 240.1f, 156.1f, 240.1f, 157.1f)
        lineTo(240.1f, 162.1f)
        cubicTo(243.1f, 167.1f, 249.1f, 170.1f, 259.1f, 172.1f)
        cubicTo(262.1f, 173.1f, 274.1f, 173.1f, 277.1f, 172.1f)
        cubicTo(288.1f, 170.1f, 296.1f, 164.1f, 299.1f, 158.1f)
        cubicTo(299.1f, 147.1f, 302.1f, 135.1f, 308.1f, 137.1f)
        lineTo(314.1f, 137.1f)
        cubicTo(314.1f, 138.1f, 314.1f, 137.1f, 316.1f, 136.1f)
        cubicTo(316.1f, 134.1f, 319.1f, 132.1f, 322.1f, 131.1f)
        cubicTo(325.1f, 127.1f, 325.1f, 128.1f, 325.1f, 124.1f)
        lineTo(325.1f, 119.1f)
        cubicTo(325.1f, 115.1f, 328.1f, 113.1f, 334.1f, 114.1f)
        cubicTo(339.1f, 114.1f, 339.1f, 114.1f, 339.1f, 115.1f)
        cubicTo(345.1f, 117.1f, 348.1f, 122.1f, 351.1f, 130.1f)
        cubicTo(353.1f, 135.1f, 353.1f, 140.1f, 353.1f, 146.1f)
        cubicTo(353.1f, 153.1f, 353.1f, 163.1f, 351.1f, 169.1f)
        cubicTo(348.1f, 187.1f, 328.1f, 207.1f, 305.1f, 220.1f)
        cubicTo(296.1f, 224.1f, 285.1f, 228.1f, 274.1f, 231.1f)
        cubicTo(259.1f, 234.1f, 252.1f, 237.1f, 240.1f, 237.1f)
        close()

        moveTo(115.1f, 186.1f)
        cubicTo(113.1f, 186.1f, 109.1f, 184.1f, 106.1f, 183.1f)
        cubicTo(103.1f, 181.1f, 98.1f, 176.1f, 91.1f, 169.1f)
        cubicTo(85.1f, 163.1f, 81.7f, 155.1f, 77.9f, 145.1f)
        cubicTo(75.1f, 132.1f, 73.2f, 120.1f, 73.2f, 112.1f)
        cubicTo(75.1f, 99.1f, 81.7f, 83.1f, 90.1f, 71.2f)
        cubicTo(98.1f, 61.3f, 109.1f, 53.3f, 123.1f, 50.4f)
        cubicTo(129.1f, 48.5f, 135.1f, 48.5f, 137.1f, 49.5f)
        lineTo(137.1f, 52.3f)
        cubicTo(137.1f, 54.2f, 138.1f, 55.2f, 138.1f, 57.3f)
        lineTo(138.1f, 68.2f)
        cubicTo(135.1f, 77.1f, 129.1f, 86.1f, 119.1f, 95.1f)
        cubicTo(113.1f, 100.1f, 111.1f, 103.1f, 110.1f, 107.1f)
        lineTo(110.1f, 122.1f)
        cubicTo(111.1f, 128.1f, 116.1f, 148.1f, 117.1f, 154.1f)
        cubicTo(119.1f, 157.1f, 124.1f, 167.1f, 128.1f, 175.1f)
        cubicTo(128.1f, 176.1f, 129.1f, 176.1f, 129.1f, 177.1f)
        cubicTo(129.1f, 178.1f, 128.1f, 179.1f, 126.1f, 179.1f)
        cubicTo(124.1f, 179.1f, 122.1f, 180.1f, 121.1f, 182.1f)
        lineTo(121.1f, 184.1f)
        lineTo(120.1f, 186.1f)
        close()
    }
}
