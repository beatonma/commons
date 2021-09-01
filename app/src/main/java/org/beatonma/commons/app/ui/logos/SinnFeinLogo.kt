package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.plotPath
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.color.PoliticalColor

class SinnFeinLogo : VectorGraphic(
    pathCount = 3,
    width = 707,
    height = 878,
    primaryColor = PoliticalColor.Party.Primary.SinnFein,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getIrelandPath(),
            color = Color(0xff086723),
        )

        paths[1] = vectorPath(
            path = getOrangeStripePath(),
            color = Color(0xfffe7b11)
        )

        paths[2] = vectorPath(
            path = getWhiteStripePath(),
            color = Color.White
        )
    }

    private fun getIrelandPath(): Path = Path().apply {
        moveTo(180f, 492f)
        lineTo(168f, 517f)
        lineTo(148f, 562f)
        lineTo(127f, 590f)
        lineTo(120f, 600f)
        lineTo(89f, 650f)
        lineTo(85f, 656f)
        lineTo(45f, 711f)
        lineTo(22f, 746f)
        lineTo(18f, 752f)
        lineTo(12f, 753f)
        lineTo(16f, 756f)
        lineTo(23f, 756f)
        lineTo(20f, 760f)
        lineTo(21f, 764f)
        lineTo(14f, 768f)
        lineTo(5f, 768f)
        lineTo(0f, 778f)
        lineTo(0f, 778f)
        lineTo(7f, 778f)
        lineTo(5f, 788f)
        lineTo(7f, 788f)
        lineTo(4f, 798f)
        lineTo(8f, 798f)
        lineTo(14f, 788f)
        cubicTo(14f, 788f, 14f, 788f, 15f, 788f)
        cubicTo(15f, 788f, 17f, 788f, 17f, 788f)
        lineTo(18f, 778f)
        lineTo(23f, 778f)
        lineTo(29f, 778f)
        lineTo(31f, 778f)
        lineTo(26f, 788f)
        lineTo(27f, 788f)
        lineTo(32f, 788f)
        lineTo(35f, 778f)
        lineTo(39f, 788f)
        lineTo(39f, 788f)
        lineTo(31f, 788f)
        lineTo(26f, 798f)
        lineTo(23f, 798f)
        lineTo(24f, 798f)
        lineTo(34f, 798f)
        lineTo(35f, 798f)
        lineTo(31f, 808f)
        lineTo(31f, 808f)
        lineTo(38f, 808f)
        lineTo(41f, 808f)
        lineTo(45f, 798f)
        lineTo(52f, 798f)
        cubicTo(52f, 798f, 57f, 798f, 58f, 798f)
        cubicTo(58f, 798f, 64f, 798f, 64f, 798f)
        lineTo(68f, 788f)
        lineTo(74f, 788f)
        lineTo(79f, 788f)
        lineTo(86f, 788f)
        lineTo(100f, 778f)
        lineTo(106f, 778f)
        lineTo(114f, 778f)
        lineTo(120f, 778f)
        lineTo(118f, 778f)
        lineTo(101f, 788f)
        lineTo(92f, 788f)
        lineTo(86f, 798f)
        lineTo(83f, 798f)
        lineTo(80f, 798f)
        lineTo(85f, 808f)
        lineTo(79f, 808f)
        lineTo(75f, 798f)
        lineTo(73f, 808f)
        lineTo(69f, 808f)
        lineTo(70f, 808f)
        lineTo(65f, 808f)
        lineTo(57f, 808f)
        lineTo(48f, 818f)
        lineTo(52f, 818f)
        lineTo(56f, 818f)
        lineTo(58f, 818f)
        lineTo(53f, 818f)
        lineTo(47f, 828f)
        lineTo(39f, 828f)
        lineTo(36f, 828f)
        lineTo(38f, 828f)
        lineTo(42f, 828f)
        lineTo(38f, 838f)
        lineTo(29f, 838f)
        lineTo(29f, 838f)
        lineTo(32f, 838f)
        lineTo(39f, 838f)
        lineTo(44f, 838f)
        lineTo(48f, 838f)
        lineTo(51f, 838f)
        lineTo(57f, 838f)
        lineTo(60f, 828f)
        lineTo(66f, 828f)
        lineTo(68f, 828f)
        lineTo(76f, 828f)
        lineTo(86f, 828f)
        lineTo(88f, 828f)
        lineTo(92f, 828f)
        lineTo(96f, 828f)
        lineTo(98f, 828f)
        lineTo(113f, 818f)
        lineTo(114f, 808f)
        lineTo(117f, 808f)
        lineTo(117f, 818f)
        lineTo(120f, 818f)
        lineTo(120f, 818f)
        lineTo(127f, 818f)
        lineTo(128f, 828f)
        lineTo(118f, 828f)
        lineTo(106f, 838f)
        lineTo(91f, 838f)
        lineTo(78f, 848f)
        lineTo(70f, 858f)
        lineTo(82f, 848f)
        lineTo(96f, 848f)
        lineTo(108f, 838f)
        lineTo(116f, 838f)
        lineTo(115f, 848f)
        lineTo(100f, 848f)
        lineTo(89f, 858f)
        lineTo(81f, 858f)
        lineTo(69f, 868f)
        lineTo(71f, 878f)
        lineTo(78f, 878f)
        lineTo(81f, 878f)
        lineTo(85f, 878f)
        lineTo(90f, 868f)
        lineTo(96f, 858f)
        lineTo(98f, 868f)
        lineTo(103f, 868f)
        lineTo(113f, 868f)
        lineTo(122f, 858f)
        lineTo(126f, 858f)
        lineTo(126f, 858f)
        lineTo(134f, 858f)
        lineTo(132f, 858f)
        lineTo(128f, 868f)
        lineTo(130f, 868f)
        lineTo(136f, 868f)
        lineTo(136f, 858f)
        lineTo(143f, 858f)
        lineTo(140f, 868f)
        lineTo(139f, 878f)
        lineTo(135f, 878f)
        lineTo(140f, 878f)
        lineTo(144f, 878f)
        lineTo(149f, 868f)
        lineTo(154f, 878f)
        lineTo(160f, 868f)
        lineTo(170f, 868f)
        lineTo(171f, 858f)
        lineTo(167f, 858f)
        lineTo(167f, 858f)
        lineTo(169f, 858f)
        lineTo(175f, 858f)
        lineTo(185f, 858f)
        lineTo(188f, 858f)
        lineTo(192f, 858f)
        lineTo(195f, 868f)
        lineTo(205f, 858f)
        lineTo(206f, 858f)
        lineTo(205f, 858f)
        lineTo(207f, 848f)
        lineTo(213f, 848f)
        lineTo(218f, 848f)
        lineTo(222f, 858f)
        lineTo(229f, 858f)
        lineTo(233f, 858f)
        lineTo(237f, 848f)
        lineTo(238f, 848f)
        lineTo(229f, 848f)
        lineTo(230f, 838f)
        lineTo(236f, 838f)
        lineTo(238f, 838f)
        lineTo(242f, 848f)
        lineTo(252f, 848f)
        cubicTo(252f, 848f, 257f, 838f, 257f, 838f)
        cubicTo(257f, 838f, 261f, 838f, 261f, 838f)
        lineTo(266f, 838f)
        lineTo(276f, 828f)
        lineTo(286f, 828f)
        lineTo(293f, 818f)
        lineTo(294f, 818f)
        lineTo(298f, 808f)
        lineTo(290f, 798f)
        lineTo(281f, 788f)
        lineTo(291f, 788f)
        lineTo(308f, 788f)
        lineTo(311f, 798f)
        lineTo(302f, 808f)
        lineTo(316f, 808f)
        cubicTo(316f, 808f, 334f, 808f, 334f, 808f)
        cubicTo(335f, 808f, 340f, 798f, 340f, 798f)
        lineTo(355f, 788f)
        lineTo(353f, 788f)
        cubicTo(353f, 788f, 359f, 778f, 358f, 778f)
        cubicTo(358f, 778f, 355f, 766f, 355f, 766f)
        lineTo(363f, 768f)
        lineTo(364f, 778f)
        lineTo(371f, 778f)
        lineTo(377f, 778f)
        cubicTo(377f, 778f, 378f, 768f, 380f, 768f)
        cubicTo(381f, 778f, 393f, 768f, 393f, 768f)
        lineTo(399f, 763f)
        lineTo(405f, 754f)
        lineTo(398f, 752f)
        lineTo(396f, 749f)
        lineTo(400f, 746f)
        lineTo(403f, 747f)
        lineTo(409f, 742f)
        lineTo(420f, 738f)
        lineTo(435f, 735f)
        lineTo(453f, 736f)
        cubicTo(453f, 736f, 460f, 726f, 461f, 726f)
        cubicTo(461f, 727f, 470f, 727f, 470f, 727f)
        lineTo(463f, 736f)
        lineTo(476f, 736f)
        lineTo(485f, 728f)
        lineTo(482f, 721f)
        lineTo(484f, 715f)
        lineTo(491f, 715f)
        lineTo(493f, 731f)
        lineTo(496f, 730f)
        lineTo(503f, 726f)
        lineTo(503f, 719f)
        lineTo(508f, 716f)
        lineTo(509f, 718f)
        lineTo(523f, 717f)
        lineTo(536f, 727f)
        lineTo(548f, 722f)
        cubicTo(548f, 722f, 552f, 718f, 553f, 718f)
        cubicTo(554f, 719f, 556f, 722f, 556f, 722f)
        lineTo(561f, 722f)
        lineTo(570f, 727f)
        lineTo(575f, 711f)
        lineTo(568f, 702f)
        lineTo(566f, 696f)
        lineTo(560f, 694f)
        lineTo(558f, 689f)
        lineTo(563f, 683f)
        lineTo(569f, 684f)
        lineTo(571f, 671f)
        lineTo(577f, 662f)
        lineTo(582f, 658f)
        lineTo(587f, 648f)
        lineTo(593f, 639f)
        lineTo(589f, 620f)
        lineTo(592f, 606f)
        lineTo(600f, 599f)
        lineTo(601f, 583f)
        lineTo(607f, 574f)
        lineTo(611f, 570f)
        lineTo(612f, 567f)
        lineTo(617f, 561f)
        lineTo(618f, 556f)
        lineTo(624f, 549f)
        lineTo(617f, 541f)
        lineTo(619f, 516f)
        lineTo(613f, 492f)
        lineTo(612f, 487f)
        lineTo(615f, 480f)
        lineTo(613f, 474f)
        lineTo(604f, 470f)
        lineTo(599f, 467f)
        lineTo(598f, 463f)
        lineTo(609f, 454f)
        lineTo(611f, 453f)
        lineTo(610f, 442f)
        lineTo(611f, 437f)
        lineTo(615f, 430f)
        lineTo(617f, 420f)
        lineTo(604f, 402f)
        lineTo(602f, 389f)
        lineTo(602f, 376f)
        lineTo(606f, 370f)
        lineTo(607f, 362f)
        lineTo(611f, 344f)
        lineTo(616f, 327f)
        lineTo(616f, 321f)
        lineTo(627f, 309f)
        lineTo(687f, 259f)
        lineTo(707f, 242f)
        lineTo(707f, 183f)
        lineTo(697f, 169f)
        lineTo(697f, 167f)
        lineTo(687f, 173f)
        lineTo(677f, 168f)
        lineTo(667f, 174f)
        lineTo(667f, 182f)
        lineTo(657f, 176f)
        lineTo(657f, 166f)
        lineTo(667f, 158f)
        lineTo(687f, 148f)
        lineTo(687f, 132f)
        lineTo(677f, 129f)
        lineTo(667f, 114f)
        lineTo(657f, 104f)
        lineTo(647f, 97f)
        lineTo(657f, 86f)
        lineTo(647f, 83f)
        lineTo(647f, 73f)
        lineTo(647f, 60f)
        lineTo(637f, 44f)
        lineTo(621f, 49f)
        lineTo(612f, 44f)
        lineTo(603f, 40f)
        lineTo(585f, 41f)
        lineTo(564f, 47f)
        lineTo(552f, 56f)
        lineTo(534f, 53f)
        lineTo(526f, 48f)
        lineTo(522f, 56f)
        lineTo(521f, 62f)
        lineTo(513f, 69f)
        lineTo(512f, 74f)
        lineTo(503f, 78f)
        lineTo(491f, 75f)
        lineTo(476f, 83f)
        lineTo(477f, 79f)
        lineTo(487f, 67f)
        lineTo(493f, 59f)
        lineTo(501f, 54f)
        lineTo(512f, 48f)
        lineTo(524f, 45f)
        lineTo(530f, 39f)
        lineTo(524f, 32f)
        lineTo(511f, 29f)
        lineTo(501f, 23f)
        lineTo(500f, 19f)
        lineTo(501f, 14f)
        lineTo(481f, 6f)
        lineTo(473f, 0f)
        lineTo(472f, 3f)
        lineTo(478f, 12f)
        lineTo(485f, 22f)
        lineTo(488f, 27f)
        cubicTo(488f, 27f, 484f, 26f, 484f, 26f)
        cubicTo(483f, 26f, 474f, 21f, 474f, 21f)
        lineTo(468f, 22f)
        lineTo(459f, 22f)
        lineTo(454f, 22f)
        lineTo(453f, 26f)
        lineTo(449f, 32f)
        lineTo(449f, 39f)
        lineTo(454f, 46f)
        lineTo(455f, 51f)
        lineTo(459f, 55f)
        lineTo(457f, 60f)
        lineTo(456f, 64f)
        lineTo(459f, 69f)
        lineTo(457f, 70f)
        lineTo(452f, 65f)
        lineTo(448f, 66f)
        lineTo(450f, 70f)
        lineTo(455f, 73f)
        lineTo(451f, 78f)
        lineTo(443f, 81f)
        lineTo(436f, 91f)
        lineTo(429f, 93f)
        lineTo(426f, 92f)
        lineTo(432f, 87f)
        lineTo(433f, 82f)
        cubicTo(433f, 82f, 439f, 78f, 440f, 79f)
        cubicTo(441f, 79f, 443f, 76f, 443f, 76f)
        lineTo(443f, 70f)
        lineTo(440f, 73f)
        lineTo(435f, 72f)
        lineTo(434f, 71f)
        lineTo(439f, 69f)
        lineTo(443f, 64f)
        lineTo(446f, 64f)
        lineTo(451f, 54f)
        lineTo(446f, 48f)
        lineTo(443f, 42f)
        lineTo(439f, 41f)
        lineTo(439f, 34f)
        lineTo(442f, 32f)
        lineTo(439f, 27f)
        lineTo(438f, 22f)
        lineTo(433f, 21f)
        lineTo(431f, 26f)
        lineTo(424f, 26f)
        lineTo(421f, 31f)
        lineTo(419f, 36f)
        lineTo(422f, 38f)
        lineTo(427f, 33f)
        lineTo(429f, 32f)
        lineTo(428f, 39f)
        lineTo(431f, 45f)
        lineTo(431f, 51f)
        lineTo(428f, 57f)
        lineTo(427f, 57f)
        lineTo(426f, 53f)
        lineTo(428f, 48f)
        lineTo(425f, 44f)
        lineTo(420f, 42f)
        lineTo(416f, 42f)
        lineTo(414f, 37f)
        lineTo(416f, 31f)
        lineTo(419f, 26f)
        lineTo(414f, 27f)
        lineTo(411f, 29f)
        lineTo(405f, 32f)
        lineTo(406f, 37f)
        lineTo(410f, 39f)
        lineTo(410f, 44f)
        lineTo(408f, 50f)
        lineTo(406f, 53f)
        lineTo(404f, 52f)
        lineTo(402f, 48f)
        lineTo(406f, 46f)
        lineTo(405f, 42f)
        lineTo(401f, 41f)
        lineTo(400f, 38f)
        lineTo(396f, 32f)
        lineTo(390f, 29f)
        lineTo(384f, 33f)
        lineTo(383f, 38f)
        lineTo(382f, 41f)
        lineTo(374f, 43f)
        lineTo(371f, 45f)
        lineTo(363f, 45f)
        lineTo(355f, 41f)
        lineTo(349f, 42f)
        lineTo(345f, 52f)
        lineTo(345f, 64f)
        lineTo(345f, 71f)
        lineTo(343f, 74f)
        lineTo(334f, 71f)
        lineTo(331f, 79f)
        lineTo(326f, 78f)
        lineTo(327f, 85f)
        lineTo(335f, 91f)
        lineTo(334f, 93f)
        lineTo(323f, 91f)
        lineTo(325f, 100f)
        lineTo(331f, 103f)
        lineTo(336f, 113f)
        lineTo(340f, 113f)
        lineTo(337f, 117f)
        lineTo(330f, 116f)
        lineTo(327f, 112f)
        lineTo(318f, 113f)
        lineTo(311f, 116f)
        lineTo(315f, 121f)
        lineTo(321f, 125f)
        lineTo(318f, 132f)
        lineTo(299f, 126f)
        lineTo(288f, 129f)
        lineTo(281f, 139f)
        lineTo(275f, 144f)
        lineTo(274f, 150f)
        lineTo(286f, 158f)
        cubicTo(286f, 158f, 290f, 162f, 290f, 162f)
        cubicTo(291f, 163f, 300f, 166f, 300f, 166f)
        lineTo(314f, 162f)
        lineTo(317f, 165f)
        lineTo(329f, 164f)
        cubicTo(329f, 164f, 341f, 159f, 341f, 159f)
        cubicTo(341f, 160f, 339f, 169f, 339f, 169f)
        lineTo(355f, 163f)
        lineTo(362f, 162f)
        lineTo(361f, 171f)
        lineTo(348f, 178f)
        lineTo(341f, 189f)
        lineTo(338f, 199f)
        lineTo(336f, 206f)
        lineTo(322f, 207f)
        lineTo(315f, 210f)
        lineTo(309f, 217f)
        lineTo(306f, 217f)
        lineTo(297f, 218f)
        lineTo(298f, 223f)
        lineTo(289f, 229f)
        lineTo(281f, 231f)
        lineTo(284f, 234f)
        lineTo(284f, 239f)
        lineTo(293f, 234f)
        lineTo(303f, 239f)
        lineTo(308f, 251f)
        lineTo(289f, 248f)
        lineTo(300f, 263f)
        lineTo(291f, 264f)
        lineTo(283f, 250f)
        lineTo(269f, 249f)
        lineTo(257f, 250f)
        lineTo(249f, 242f)
        cubicTo(249f, 242f, 230f, 236f, 230f, 237f)
        cubicTo(230f, 238f, 228f, 241f, 228f, 241f)
        lineTo(228f, 248f)
        lineTo(220f, 258f)
        lineTo(215f, 258f)
        lineTo(212f, 246f)
        lineTo(209f, 246f)
        lineTo(211f, 236f)
        lineTo(205f, 236f)
        lineTo(206f, 230f)
        lineTo(195f, 225f)
        cubicTo(195f, 225f, 189f, 230f, 188f, 230f)
        cubicTo(188f, 230f, 172f, 224f, 172f, 224f)
        lineTo(156f, 222f)
        lineTo(148f, 222f)
        lineTo(138f, 217f)
        lineTo(129f, 221f)
        lineTo(130f, 225f)
        lineTo(135f, 231f)
        lineTo(138f, 236f)
        lineTo(126f, 232f)
        lineTo(126f, 241f)
        lineTo(117f, 245f)
        lineTo(113f, 244f)
        lineTo(115f, 232f)
        lineTo(106f, 225f)
        lineTo(94f, 239f)
        lineTo(97f, 244f)
        lineTo(90f, 253f)
        lineTo(87f, 267f)
        lineTo(92f, 272f)
        lineTo(96f, 269f)
        lineTo(96f, 257f)
        lineTo(99f, 249f)
        cubicTo(99f, 249f, 105f, 241f, 106f, 242f)
        cubicTo(106f, 242f, 113f, 252f, 113f, 252f)
        lineTo(116f, 265f)
        lineTo(109f, 272f)
        lineTo(111f, 279f)
        cubicTo(111f, 279f, 119f, 291f, 119f, 291f)
        cubicTo(120f, 292f, 123f, 301f, 123f, 301f)
        lineTo(121f, 306f)
        lineTo(110f, 317f)
        lineTo(109f, 325f)
        lineTo(120f, 326f)
        lineTo(130f, 319f)
        lineTo(145f, 322f)
        lineTo(157f, 322f)
        lineTo(153f, 330f)
        lineTo(150f, 342f)
        lineTo(155f, 347f)
        lineTo(107f, 351f)
        lineTo(105f, 364f)
        lineTo(107f, 373f)
        lineTo(106f, 376f)
        lineTo(117f, 382f)
        lineTo(126f, 386f)
        lineTo(128f, 389f)
        cubicTo(128f, 389f, 117f, 389f, 117f, 389f)
        cubicTo(117f, 388f, 109f, 382f, 109f, 382f)
        lineTo(103f, 386f)
        lineTo(87f, 382f)
        lineTo(89f, 390f)
        lineTo(85f, 393f)
        lineTo(74f, 394f)
        lineTo(64f, 394f)
        lineTo(71f, 400f)
        lineTo(74f, 408f)
        lineTo(78f, 416f)
        cubicTo(78f, 416f, 84f, 417f, 84f, 417f)
        cubicTo(83f, 418f, 79f, 420f, 79f, 420f)
        cubicTo(78f, 420f, 70f, 413f, 70f, 414f)
        cubicTo(70f, 414f, 69f, 420f, 69f, 420f)
        cubicTo(69f, 420f, 65f, 424f, 64f, 425f)
        cubicTo(63f, 426f, 76f, 428f, 76f, 428f)
        lineTo(79f, 427f)
        lineTo(82f, 434f)
        lineTo(97f, 435f)
        lineTo(101f, 426f)
        cubicTo(101f, 426f, 103f, 432f, 105f, 432f)
        cubicTo(106f, 432f, 111f, 426f, 111f, 427f)
        cubicTo(111f, 427f, 115f, 426f, 115f, 428f)
        cubicTo(115f, 429f, 115f, 435f, 115f, 436f)
        cubicTo(115f, 436f, 102f, 441f, 102f, 441f)
        lineTo(106f, 448f)
        lineTo(115f, 457f)
        lineTo(122f, 452f)
        lineTo(127f, 442f)
        lineTo(135f, 438f)
        lineTo(136f, 449f)
        lineTo(139f, 462f)
        lineTo(145f, 473f)
        cubicTo(145f, 473f, 148f, 477f, 150f, 477f)
        cubicTo(151f, 477f, 170f, 472f, 170f, 472f)
        lineTo(181f, 471f)
        cubicTo(181f, 471f, 192f, 470f, 193f, 471f)
        cubicTo(193f, 472f, 197f, 479f, 197f, 479f)
        lineTo(193f, 486f)
        lineTo(197f, 494f)
        lineTo(192f, 499f)
        lineTo(182f, 492f)
        close()
    }

    private fun getOrangeStripePath() = plotPath {
        moveTo(384f, 505f)
        cubicTo(372f, 531f, 362f, 545f, 343f, 565f)
        cubicTo(276f, 629f, 166f, 645f, 89f, 650f)
        lineTo(127f, 590f)
        cubicTo(169f, 597f, 213f, 595f, 248f, 593f)
        cubicTo(284f, 591f, 315f, 578f, 341f, 558f)
        cubicTo(365f, 540f, 370f, 527f, 384f, 505f)
        close()

        moveTo(420f, 403f)
        cubicTo(436f, 361f, 453f, 342f, 487f, 314f)
        cubicTo(516f, 294f, 545f, 285f, 578f, 273f)
        cubicTo(616f, 263f, 647f, 261f, 687f, 259f)
        lineTo(627f, 309f)
        cubicTo(579f, 305f, 538f, 308f, 492f, 327f)
        cubicTo(450f, 346f, 439f, 370f, 420f, 403f)
        close()
    }

    private fun getWhiteStripePath() = plotPath {
        moveTo(383f, 529f)
        cubicTo(368f, 558f, 349f, 578f, 328f, 603f)
        cubicTo(267f, 663f, 181f, 706f, 95f, 727f)
        lineTo(24f, 743f)
        cubicTo(42f, 711f, 63f, 686f, 77f, 666f)
        cubicTo(135f, 665f, 191f, 656f, 239f, 639f)
        cubicTo(273f, 627f, 300f, 612f, 330f, 590f)
        cubicTo(357f, 569f, 366f, 552f, 383f, 529f)
        close()

        moveTo(420f, 379f)
        cubicTo(428f, 356f, 441f, 335f, 456f, 316f)
        cubicTo(505f, 254f, 568f, 219f, 637f, 198f)
        cubicTo(657f, 191f, 687f, 189f, 707f, 186f)
        lineTo(707f, 242f)
        lineTo(677f, 243f)
        cubicTo(621f, 245f, 571f, 257f, 528f, 276f)
        cubicTo(472f, 302f, 444f, 330f, 420f, 379f)
        close()
    }
}
