package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.plotPath
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.color.PoliticalColor

class ConservativeLogo : VectorGraphic(
    pathCount = 3,
    width = 690,
    height = 523,
    primaryColor = PoliticalColor.Party.Primary.Conservative,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getBlueFieldPath(),
            color = Color(0xff11437d),
            style = Fill
        )
        paths[1] = vectorPath(
            path = getWhiteTreePath(),
            color = Color.White,
            style = Fill
        )
        paths[2] = vectorPath(
            path = getRedCrossPath(),
            color = Color(0xffe63329),
            style = Fill
        )
    }

    private fun getBlueFieldPath() = plotPath {
        moveTo(501f, 510f)
        cubicTo(501f, 510f, 503f, 500f, 502f, 500f)
        cubicTo(445f, 500f, 335f, 500f, 325f, 490f)
        cubicTo(317f, 490f, 329f, 460f, 336f, 440f)
        cubicTo(336f, 440f, 325f, 420f, 305f, 400f)
        cubicTo(303f, 400f, 274f, 430f, 274f, 490f)
        cubicTo(250f, 490f, 248f, 490f, 220f, 500f)
        cubicTo(201f, 500f, 100f, 500f, 100f, 500f)
        lineTo(96.3f, 500f)
        cubicTo(91.3f, 500f, 60f, 510f, 60f, 510f)
        cubicTo(60f, 510f, 62.3f, 510f, 65.8f, 520f)
        cubicTo(79.3f, 520f, 274f, 530f, 293f, 510f)
        cubicTo(302f, 520f, 318f, 520f, 349f, 520f)
        cubicTo(356f, 520f, 458f, 520f, 471f, 520f)
        cubicTo(513f, 520f, 602f, 520f, 602f, 520f)
        lineTo(609f, 510f)
        cubicTo(600f, 510f, 554f, 510f, 525f, 510f)
        cubicTo(517f, 510f, 503f, 510f, 501f, 510f)
        moveTo(245f, 410f)
        cubicTo(282f, 400f, 310f, 380f, 310f, 380f)
        cubicTo(310f, 380f, 316f, 390f, 322f, 400f)
        lineTo(347f, 430f)
        cubicTo(347f, 430f, 374f, 420f, 406f, 410f)
        lineTo(267f, 330f)
        lineTo(244f, 410f)
        cubicTo(244f, 410f, 245f, 410f, 245f, 410f)
        moveTo(18.6f, 370f)
        lineTo(67.6f, 430f)
        lineTo(100f, 410f)
        lineTo(126f, 330f)
        close()

        moveTo(551f, 150f)
        lineTo(553f, 150f)
        lineTo(547f, 140f)
        lineTo(456f, 180f)
        lineTo(573f, 180f)
        close()

        moveTo(130f, 150f)
        lineTo(130f, 150f)
        cubicTo(130f, 150f, 132f, 150f, 133f, 150f)
        cubicTo(133f, 150f, 132f, 150f, 132f, 150f)
        lineTo(162f, 170f)
        lineTo(195f, 60f)
        cubicTo(150f, 70f, 125f, 90f, 93.3f, 120f)
        lineTo(92.3f, 110f)
        cubicTo(97.3f, 100f, 124f, 80f, 140f, 70f)
        cubicTo(162f, 50f, 195f, 40f, 195f, 40f)
        lineTo(199f, 10f)
        cubicTo(158f, 20f, 122f, 40f, 84.3f, 70f)
        cubicTo(66.4f, 80f, 60f, 90f, 53.2f, 110f)
        close()

        moveTo(336f, 10f)
        lineTo(302f, 170f)
        lineTo(485f, 80f)
        lineTo(478f, 70f)
        lineTo(421f, 100f)
        lineTo(395f, 60f)
        lineTo(326f, 90f)
        cubicTo(334f, 80f, 338f, 70f, 360f, 60f)
        cubicTo(350f, 40f, 341f, 20f, 336f, 10f)
        moveTo(664f, 320f)
        lineTo(628f, 320f)
        cubicTo(621f, 320f, 615f, 330f, 607f, 330f)
        cubicTo(594f, 340f, 576f, 350f, 576f, 350f)
        cubicTo(584f, 350f, 596f, 330f, 605f, 320f)
        lineTo(424f, 320f)
        lineTo(581f, 410f)
        cubicTo(599f, 400f, 614f, 390f, 625f, 380f)
        cubicTo(625f, 380f, 613f, 400f, 603f, 410f)
        cubicTo(616f, 420f, 630f, 430f, 630f, 430f)
        lineTo(690f, 340f)
        close()
    }

    private fun getWhiteTreePath() = plotPath {
        moveTo(100f, 410f)
        lineTo(129f, 390f)
        lineTo(130f, 390f)
        lineTo(134f, 400f)
        lineTo(161f, 290f)
        lineTo(90.3f, 290f)
        cubicTo(80.3f, 300f, 65.8f, 310f, 60f, 320f)
        lineTo(122f, 320f)
        lineTo(28f, 350f)
        cubicTo(17.2f, 360f, 12.9f, 370f, 12.9f, 370f)
        lineTo(17.2f, 370f)
        lineTo(125f, 330f)
        close()

        moveTo(65.8f, 290f)
        lineTo(26.7f, 290f)
        lineTo(47.4f, 310f)
        cubicTo(47.4f, 310f, 55f, 300f, 65.8f, 290f)
        moveTo(162f, 170f)
        lineTo(132f, 150f)
        cubicTo(131f, 150f, 100f, 170f, 65.8f, 200f)
        cubicTo(65.1f, 200f, 63.7f, 200f, 60.8f, 210f)
        lineTo(186f, 210f)
        lineTo(220f, 40f)
        cubicTo(214f, 50f, 203f, 50f, 193f, 60f)
        close()

        moveTo(473f, 400f)
        cubicTo(469f, 410f, 465f, 420f, 465f, 420f)
        lineTo(504f, 450f)
        cubicTo(504f, 450f, 514f, 440f, 536f, 430f)
        close()

        moveTo(244f, 410f)
        lineTo(267f, 330f)
        lineTo(406f, 410f)
        cubicTo(410f, 410f, 415f, 410f, 421f, 400f)
        cubicTo(432f, 400f, 444f, 390f, 454f, 390f)
        lineTo(335f, 320f)
        lineTo(394f, 320f)
        lineTo(482f, 370f)
        cubicTo(486f, 370f, 488f, 360f, 488f, 360f)
        cubicTo(488f, 360f, 487f, 370f, 485f, 370f)
        lineTo(566f, 420f)
        cubicTo(571f, 410f, 575f, 410f, 581f, 410f)
        lineTo(424f, 320f)
        lineTo(606f, 320f)
        cubicTo(615f, 300f, 623f, 290f, 625f, 290f)
        lineTo(244f, 290f)
        lineTo(219f, 420f)
        cubicTo(225f, 420f, 234f, 410f, 244f, 410f)
        moveTo(21.4f, 180f)
        lineTo(9.9f, 170f)
        cubicTo(5.4f, 180f, 2.2f, 190f, 0f, 200f)
        cubicTo(1.6f, 200f, 3.2f, 200f, 5.4f, 210f)
        lineTo(42.2f, 210f)
        cubicTo(49.4f, 200f, 60f, 190f, 78.6f, 180f)
        close()

        moveTo(570f, 180f)
        lineTo(454f, 180f)
        lineTo(544f, 140f)
        lineTo(521f, 110f)
        lineTo(365f, 180f)
        lineTo(306f, 180f)
        lineTo(505f, 90f)
        lineTo(493f, 90f)
        lineTo(485f, 80f)
        lineTo(302f, 170f)
        lineTo(336f, 10f)
        cubicTo(334f, 10f, 333f, 0f, 333f, 0f)
        cubicTo(333f, 0f, 324f, 10f, 309f, 10f)
        lineTo(270f, 210f)
        lineTo(594f, 200f)
        close()

        moveTo(664f, 320f)
        lineTo(644f, 300f)
        cubicTo(644f, 300f, 637f, 310f, 628f, 320f)
        close()

        moveTo(81.3f, 180f)
        cubicTo(94.3f, 170f, 120f, 160f, 130f, 150f)
        lineTo(53.2f, 110f)
        cubicTo(45.4f, 120f, 37.8f, 130f, 29.3f, 150f)
        close()
    }

    private fun getRedCrossPath() = plotPath {
        moveTo(28f, 350f)
        cubicTo(37.8f, 350f, 48.4f, 330f, 60f, 320f)
        lineTo(122f, 320f)
        close()

        moveTo(12.9f, 170f)
        lineTo(21.4f, 180f)
        lineTo(78.6f, 180f)
        cubicTo(79.3f, 180f, 80.3f, 180f, 81.3f, 180f)
        lineTo(29.3f, 150f)
        cubicTo(22.8f, 160f, 17.2f, 170f, 12.9f, 170f)
        moveTo(485f, 370f)
        cubicTo(482f, 380f, 477f, 380f, 474f, 390f)
        cubicTo(474f, 390f, 473f, 400f, 473f, 400f)
        lineTo(535f, 430f)
        cubicTo(543f, 430f, 553f, 430f, 564f, 420f)
        cubicTo(565f, 420f, 565f, 420f, 566f, 420f)
        close()

        moveTo(625f, 290f)
        cubicTo(614f, 270f, 580f, 240f, 580f, 240f)
        cubicTo(580f, 240f, 541f, 270f, 512f, 280f)
        cubicTo(512f, 280f, 538f, 260f, 546f, 250f)
        cubicTo(558f, 240f, 594f, 210f, 594f, 210f)
        lineTo(594f, 200f)
        lineTo(270f, 210f)
        lineTo(309f, 10f)
        cubicTo(292f, 10f, 267f, 20f, 233f, 40f)
        cubicTo(229f, 40f, 225f, 40f, 220f, 40f)
        lineTo(186f, 210f)
        lineTo(60.8f, 210f)
        cubicTo(30.6f, 240f, 0.6f, 270f, 0.6f, 270f)
        lineTo(26.7f, 290f)
        lineTo(65.8f, 290f)
        cubicTo(84.3f, 280f, 100f, 260f, 132f, 250f)
        lineTo(127f, 260f)
        cubicTo(120f, 270f, 100f, 280f, 90.3f, 290f)
        lineTo(161f, 290f)
        lineTo(134f, 400f)
        lineTo(168f, 430f)
        lineTo(171f, 430f)
        cubicTo(171f, 430f, 195f, 430f, 219f, 420f)
        lineTo(244f, 290f)
        lineTo(625f, 290f)
        cubicTo(625f, 290f, 625f, 290f, 625f, 290f)
        moveTo(482f, 370f)
        lineTo(394f, 320f)
        lineTo(335f, 320f)
        lineTo(454f, 390f)
        cubicTo(466f, 380f, 476f, 370f, 482f, 370f)
        moveTo(521f, 110f)
        lineTo(505f, 90f)
        lineTo(306f, 180f)
        lineTo(365f, 180f)
        close()

        moveTo(22.8f, 220f)
        cubicTo(22.8f, 220f, 30.6f, 210f, 42.2f, 210f)
        lineTo(5.4f, 210f)
        cubicTo(14.4f, 210f, 22.8f, 220f, 22.8f, 220f)
    }
}
