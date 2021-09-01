package org.beatonma.commons.app.ui.logos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.svg.vectorPath
import org.beatonma.commons.theme.color.PoliticalColor

class PlaidCymruLogo : VectorGraphic(
    pathCount = 6,
    width = 1335,
    height = 1342,
    primaryColor = PoliticalColor.Party.Primary.PlaidCymru,
) {

    override fun buildPaths() {
        paths[0] = vectorPath(
            path = getPath1(),
            color = Color(0xfff8b909),
        )

        paths[1] = vectorPath(
            path = getPath2(),
            color = Color(0xfffbc707),
        )

        paths[2] = vectorPath(
            path = getPath3(),
            color = Color(0xfff5af0b),
        )

        paths[3] = vectorPath(
            path = getPath4(),
            color = Color(0xffe8860f),
        )

        paths[4] = vectorPath(
            path = getPath5(),
            color = Color(0xffef9b0d),
        )

        paths[5] = vectorPath(
            path = getPath6(),
            color = Color(0xffdf6713),
        )
    }

    private fun getPath1(): Path = Path().apply {
        moveTo(204.54f, 477.2f)
        cubicTo(106.32f, 520.46f, 19.72f, 582.89f, 2.94f, 647.65f)
        cubicTo(2.93f, 647.67f, 2.92f, 647.7f, 2.91f, 647.72f)
        cubicTo(2.91f, 647.73f, 2.91f, 647.74f, 2.91f, 647.74f)
        cubicTo(1f, 655.14f, 0f, 662.57f, 0f, 670f)
        cubicTo(0f, 670.62f, 0.01f, 671.26f, 0.03f, 671.88f)
        cubicTo(0.03f, 671.91f, 0.03f, 671.97f, 0.03f, 672f)
        cubicTo(0.03f, 672.43f, 0.05f, 672.87f, 0.07f, 673.3f)
        cubicTo(0.07f, 673.4f, 0.08f, 673.51f, 0.08f, 673.61f)
        cubicTo(0.08f, 673.61f, 0.08f, 673.62f, 0.08f, 673.62f)
        cubicTo(3.17f, 744.92f, 96.88f, 815.37f, 204.53f, 862.79f)
        cubicTo(233.5f, 788.24f, 274.55f, 716.66f, 315.61f, 670f)
        cubicTo(274.55f, 623.34f, 233.5f, 551.75f, 204.53f, 477.2f)
        moveTo(1135.46f, 477.21f)
        cubicTo(1106.5f, 551.76f, 1065.44f, 623.35f, 1024.39f, 670f)
        cubicTo(1065.44f, 716.65f, 1106.5f, 788.24f, 1135.46f, 862.79f)
        cubicTo(1244.09f, 814.95f, 1338.51f, 743.65f, 1339.98f, 671.7f)
        cubicTo(1339.99f, 671.13f, 1340f, 670.57f, 1340f, 670f)
        cubicTo(1340f, 669.1f, 1339.98f, 668.21f, 1339.95f, 667.32f)
        cubicTo(1339.95f, 667.26f, 1339.94f, 667.19f, 1339.94f, 667.12f)
        cubicTo(1339.62f, 657.85f, 1337.77f, 648.59f, 1334.54f, 639.39f)
        cubicTo(1312.89f, 577.61f, 1229.49f, 518.62f, 1135.46f, 477.21f)
        moveTo(670f, 1024.39f)
        cubicTo(623.34f, 1065.44f, 551.76f, 1106.5f, 477.21f, 1135.46f)
        cubicTo(521f, 1234.89f, 584.44f, 1322.41f, 650.03f, 1337.65f)
        cubicTo(650.04f, 1337.65f, 650.05f, 1337.66f, 650.06f, 1337.66f)
        cubicTo(650.09f, 1337.66f, 650.12f, 1337.67f, 650.15f, 1337.67f)
        cubicTo(650.19f, 1337.68f, 650.23f, 1337.69f, 650.27f, 1337.71f)
        cubicTo(650.34f, 1337.72f, 650.41f, 1337.74f, 650.48f, 1337.75f)
        cubicTo(650.54f, 1337.77f, 650.61f, 1337.78f, 650.67f, 1337.8f)
        cubicTo(650.67f, 1337.81f, 650.68f, 1337.81f, 650.69f, 1337.81f)
        cubicTo(650.73f, 1337.81f, 650.76f, 1337.82f, 650.8f, 1337.82f)
        cubicTo(650.83f, 1337.83f, 650.86f, 1337.84f, 650.9f, 1337.85f)
        cubicTo(651.02f, 1337.87f, 651.13f, 1337.9f, 651.25f, 1337.93f)
        cubicTo(656.07f, 1338.98f, 660.91f, 1339.64f, 665.75f, 1339.89f)
        cubicTo(665.93f, 1339.89f, 666.12f, 1339.91f, 666.3f, 1339.92f)
        cubicTo(666.45f, 1339.92f, 666.58f, 1339.93f, 666.72f, 1339.94f)
        cubicTo(666.98f, 1339.95f, 667.24f, 1339.95f, 667.5f, 1339.96f)
        cubicTo(668.33f, 1339.99f, 669.17f, 1340f, 670f, 1340f)
        cubicTo(670.63f, 1340f, 671.25f, 1339.99f, 671.88f, 1339.98f)
        lineTo(671.93f, 1339.98f)
        cubicTo(672.1f, 1339.97f, 672.27f, 1339.97f, 672.44f, 1339.96f)
        cubicTo(672.83f, 1339.95f, 673.22f, 1339.94f, 673.61f, 1339.92f)
        lineTo(673.63f, 1339.92f)
        cubicTo(673.96f, 1339.9f, 674.28f, 1339.88f, 674.61f, 1339.87f)
        cubicTo(674.77f, 1339.86f, 674.93f, 1339.85f, 675.09f, 1339.84f)
        lineTo(675.1f, 1339.84f)
        cubicTo(677.43f, 1339.69f, 679.77f, 1339.46f, 682.1f, 1339.13f)
        lineTo(682.11f, 1339.13f)
        cubicTo(682.17f, 1339.12f, 682.24f, 1339.1f, 682.3f, 1339.1f)
        cubicTo(682.3f, 1339.1f, 682.3f, 1339.1f, 682.31f, 1339.1f)
        cubicTo(682.34f, 1339.09f, 682.36f, 1339.09f, 682.38f, 1339.09f)
        cubicTo(682.5f, 1339.07f, 682.62f, 1339.05f, 682.74f, 1339.03f)
        cubicTo(682.88f, 1339.02f, 683.01f, 1338.99f, 683.15f, 1338.96f)
        lineTo(683.16f, 1338.96f)
        cubicTo(683.21f, 1338.96f, 683.26f, 1338.95f, 683.3f, 1338.95f)
        cubicTo(683.33f, 1338.94f, 683.34f, 1338.94f, 683.38f, 1338.94f)
        cubicTo(683.39f, 1338.93f, 683.4f, 1338.93f, 683.42f, 1338.93f)
        cubicTo(751.33f, 1328.21f, 817.54f, 1238.19f, 862.8f, 1135.46f)
        cubicTo(788.24f, 1106.5f, 716.65f, 1065.44f, 670f, 1024.38f)
        moveTo(670f, 0f)
        lineTo(670f, 0f)
        cubicTo(668.76f, 0f, 667.53f, 0.03f, 666.3f, 0.09f)
        cubicTo(666.12f, 0.09f, 665.93f, 0.1f, 665.75f, 0.11f)
        cubicTo(665.62f, 0.11f, 665.5f, 0.12f, 665.37f, 0.13f)
        cubicTo(665.36f, 0.13f, 665.34f, 0.13f, 665.32f, 0.13f)
        cubicTo(665.31f, 0.13f, 665.3f, 0.13f, 665.29f, 0.13f)
        cubicTo(665.16f, 0.14f, 665.04f, 0.15f, 664.9f, 0.16f)
        cubicTo(664.88f, 0.16f, 664.86f, 0.16f, 664.84f, 0.16f)
        cubicTo(664.73f, 0.16f, 664.63f, 0.17f, 664.53f, 0.18f)
        cubicTo(664.47f, 0.18f, 664.41f, 0.19f, 664.36f, 0.19f)
        cubicTo(664.33f, 0.19f, 664.28f, 0.2f, 664.25f, 0.2f)
        cubicTo(664.12f, 0.21f, 663.98f, 0.22f, 663.85f, 0.23f)
        lineTo(663.84f, 0.23f)
        cubicTo(663.62f, 0.24f, 663.43f, 0.26f, 663.22f, 0.28f)
        cubicTo(663.21f, 0.28f, 663.2f, 0.28f, 663.2f, 0.28f)
        cubicTo(663.16f, 0.28f, 663.12f, 0.29f, 663.08f, 0.29f)
        cubicTo(663.05f, 0.29f, 663.02f, 0.3f, 662.98f, 0.3f)
        cubicTo(662.78f, 0.31f, 662.57f, 0.33f, 662.36f, 0.35f)
        lineTo(662.35f, 0.35f)
        cubicTo(662.33f, 0.36f, 662.32f, 0.36f, 662.3f, 0.36f)
        cubicTo(662.18f, 0.37f, 662.05f, 0.38f, 661.92f, 0.39f)
        cubicTo(661.68f, 0.42f, 661.44f, 0.43f, 661.2f, 0.46f)
        cubicTo(591.69f, 7.72f, 523.48f, 99.48f, 477.2f, 204.53f)
        cubicTo(551.75f, 233.5f, 623.34f, 274.56f, 670f, 315.61f)
        cubicTo(716.65f, 274.55f, 788.24f, 233.5f, 862.79f, 204.53f)
        cubicTo(815.37f, 96.88f, 744.92f, 3.17f, 673.63f, 0.08f)
        lineTo(673.61f, 0.08f)
        cubicTo(673.52f, 0.08f, 673.41f, 0.07f, 673.32f, 0.07f)
        cubicTo(672.85f, 0.05f, 672.39f, 0.04f, 671.93f, 0.03f)
        lineTo(671.88f, 0.03f)
        cubicTo(671.25f, 0.01f, 670.62f, 0f, 670f, 0f)
    }

    private fun getPath2(): Path = Path().apply {
        moveTo(204.54f, 862.8f)
        cubicTo(165.68f, 962.84f, 148.59f, 1068.22f, 182.5f, 1125.88f)
        cubicTo(182.52f, 1125.9f, 182.54f, 1125.93f, 182.55f, 1125.95f)
        cubicTo(182.55f, 1125.96f, 182.55f, 1125.97f, 182.55f, 1125.97f)
        cubicTo(186.43f, 1132.55f, 190.98f, 1138.5f, 196.24f, 1143.75f)
        cubicTo(196.68f, 1144.2f, 197.13f, 1144.64f, 197.58f, 1145.07f)
        cubicTo(197.62f, 1145.1f, 197.64f, 1145.13f, 197.67f, 1145.16f)
        cubicTo(197.99f, 1145.45f, 198.3f, 1145.76f, 198.62f, 1146.04f)
        cubicTo(198.7f, 1146.12f, 198.78f, 1146.19f, 198.85f, 1146.26f)
        cubicTo(198.85f, 1146.26f, 198.85f, 1146.27f, 198.86f, 1146.27f)
        cubicTo(251.46f, 1194.49f, 367.54f, 1178.04f, 477.2f, 1135.46f)
        cubicTo(444.95f, 1062.27f, 423.37f, 982.61f, 419.4f, 920.59f)
        cubicTo(357.39f, 916.64f, 277.74f, 895.04f, 204.54f, 862.81f)
        moveTo(862.81f, 204.54f)
        cubicTo(895.04f, 277.73f, 916.63f, 357.39f, 920.59f, 419.41f)
        cubicTo(982.61f, 423.37f, 1062.26f, 444.95f, 1135.46f, 477.19f)
        cubicTo(1178.43f, 366.55f, 1194.79f, 249.37f, 1144.95f, 197.45f)
        cubicTo(1144.55f, 197.05f, 1144.16f, 196.64f, 1143.75f, 196.24f)
        cubicTo(1143.12f, 195.61f, 1142.49f, 194.99f, 1141.84f, 194.38f)
        cubicTo(1141.79f, 194.33f, 1141.74f, 194.28f, 1141.69f, 194.24f)
        cubicTo(1134.91f, 187.91f, 1127.04f, 182.67f, 1118.26f, 178.44f)
        cubicTo(1059.26f, 150.08f, 958.59f, 167.33f, 862.81f, 204.54f)
        moveTo(920.59f, 920.59f)
        cubicTo(916.63f, 982.61f, 895.05f, 1062.26f, 862.8f, 1135.46f)
        cubicTo(964.07f, 1174.79f, 1070.82f, 1191.83f, 1127.99f, 1156.22f)
        cubicTo(1127.99f, 1156.21f, 1128f, 1156.21f, 1128f, 1156.2f)
        cubicTo(1128.02f, 1156.19f, 1128.06f, 1156.16f, 1128.08f, 1156.15f)
        cubicTo(1128.12f, 1156.13f, 1128.15f, 1156.11f, 1128.19f, 1156.09f)
        cubicTo(1128.25f, 1156.04f, 1128.31f, 1156.01f, 1128.37f, 1155.97f)
        cubicTo(1128.42f, 1155.94f, 1128.48f, 1155.91f, 1128.54f, 1155.87f)
        cubicTo(1128.55f, 1155.86f, 1128.55f, 1155.86f, 1128.55f, 1155.86f)
        cubicTo(1128.59f, 1155.83f, 1128.61f, 1155.82f, 1128.65f, 1155.8f)
        cubicTo(1128.67f, 1155.77f, 1128.71f, 1155.76f, 1128.73f, 1155.74f)
        cubicTo(1128.85f, 1155.68f, 1128.93f, 1155.61f, 1129.04f, 1155.55f)
        cubicTo(1133.19f, 1152.88f, 1137.08f, 1149.93f, 1140.68f, 1146.68f)
        cubicTo(1140.82f, 1146.56f, 1140.95f, 1146.44f, 1141.08f, 1146.32f)
        cubicTo(1141.19f, 1146.22f, 1141.29f, 1146.12f, 1141.4f, 1146.03f)
        cubicTo(1141.59f, 1145.85f, 1141.78f, 1145.67f, 1141.97f, 1145.49f)
        cubicTo(1142.57f, 1144.93f, 1143.17f, 1144.34f, 1143.76f, 1143.75f)
        cubicTo(1144.2f, 1143.31f, 1144.64f, 1142.86f, 1145.08f, 1142.4f)
        cubicTo(1145.09f, 1142.4f, 1145.1f, 1142.38f, 1145.11f, 1142.37f)
        cubicTo(1145.22f, 1142.25f, 1145.35f, 1142.13f, 1145.46f, 1142.01f)
        cubicTo(1145.73f, 1141.72f, 1145.99f, 1141.44f, 1146.26f, 1141.14f)
        cubicTo(1146.27f, 1141.14f, 1146.27f, 1141.13f, 1146.27f, 1141.13f)
        cubicTo(1146.5f, 1140.88f, 1146.71f, 1140.65f, 1146.93f, 1140.4f)
        cubicTo(1147.03f, 1140.29f, 1147.15f, 1140.16f, 1147.24f, 1140.04f)
        cubicTo(1147.25f, 1140.04f, 1147.25f, 1140.04f, 1147.26f, 1140.04f)
        cubicTo(1148.8f, 1138.29f, 1150.29f, 1136.47f, 1151.7f, 1134.58f)
        lineTo(1151.7f, 1134.58f)
        cubicTo(1151.74f, 1134.53f, 1151.78f, 1134.47f, 1151.81f, 1134.42f)
        cubicTo(1151.82f, 1134.42f, 1151.83f, 1134.41f, 1151.83f, 1134.41f)
        cubicTo(1151.84f, 1134.39f, 1151.86f, 1134.37f, 1151.87f, 1134.36f)
        cubicTo(1151.94f, 1134.26f, 1152.01f, 1134.16f, 1152.08f, 1134.06f)
        cubicTo(1152.17f, 1133.96f, 1152.25f, 1133.84f, 1152.33f, 1133.73f)
        cubicTo(1152.33f, 1133.73f, 1152.33f, 1133.72f, 1152.34f, 1133.72f)
        cubicTo(1152.37f, 1133.68f, 1152.4f, 1133.64f, 1152.42f, 1133.61f)
        cubicTo(1152.44f, 1133.58f, 1152.46f, 1133.57f, 1152.47f, 1133.55f)
        cubicTo(1152.48f, 1133.53f, 1152.49f, 1133.52f, 1152.49f, 1133.5f)
        cubicTo(1192.94f, 1077.91f, 1176.1f, 967.43f, 1135.46f, 862.8f)
        cubicTo(1062.26f, 895.04f, 982.61f, 916.62f, 920.59f, 920.59f)
        moveTo(196.24f, 196.24f)
        lineTo(196.24f, 196.24f)
        cubicTo(195.36f, 197.11f, 194.51f, 198f, 193.68f, 198.91f)
        cubicTo(193.56f, 199.04f, 193.43f, 199.18f, 193.31f, 199.32f)
        cubicTo(193.22f, 199.42f, 193.14f, 199.5f, 193.06f, 199.6f)
        cubicTo(193.05f, 199.62f, 193.03f, 199.63f, 193.03f, 199.63f)
        cubicTo(193.02f, 199.64f, 193.01f, 199.65f, 193.01f, 199.66f)
        cubicTo(192.91f, 199.75f, 192.83f, 199.85f, 192.74f, 199.95f)
        cubicTo(192.73f, 199.96f, 192.71f, 199.98f, 192.7f, 200f)
        cubicTo(192.64f, 200.08f, 192.57f, 200.15f, 192.5f, 200.23f)
        cubicTo(192.45f, 200.27f, 192.43f, 200.32f, 192.38f, 200.36f)
        cubicTo(192.37f, 200.39f, 192.34f, 200.41f, 192.32f, 200.44f)
        cubicTo(192.23f, 200.54f, 192.14f, 200.65f, 192.06f, 200.74f)
        cubicTo(192.05f, 200.75f, 192.05f, 200.75f, 192.05f, 200.76f)
        cubicTo(191.91f, 200.92f, 191.77f, 201.07f, 191.64f, 201.23f)
        cubicTo(191.63f, 201.24f, 191.63f, 201.24f, 191.63f, 201.24f)
        cubicTo(191.6f, 201.27f, 191.58f, 201.3f, 191.55f, 201.33f)
        cubicTo(191.53f, 201.35f, 191.51f, 201.38f, 191.49f, 201.4f)
        cubicTo(191.35f, 201.56f, 191.22f, 201.72f, 191.08f, 201.89f)
        cubicTo(191.08f, 201.89f, 191.08f, 201.89f, 191.08f, 201.89f)
        cubicTo(191.07f, 201.9f, 191.06f, 201.92f, 191.04f, 201.93f)
        cubicTo(190.96f, 202.03f, 190.89f, 202.13f, 190.81f, 202.22f)
        cubicTo(190.64f, 202.41f, 190.5f, 202.6f, 190.35f, 202.79f)
        cubicTo(146.32f, 257.07f, 162.98f, 370.19f, 204.53f, 477.19f)
        cubicTo(277.74f, 444.95f, 357.39f, 423.37f, 419.4f, 419.41f)
        cubicTo(423.37f, 357.39f, 444.95f, 277.74f, 477.19f, 204.54f)
        cubicTo(367.53f, 161.94f, 251.46f, 145.5f, 198.86f, 193.73f)
        cubicTo(198.86f, 193.73f, 198.85f, 193.74f, 198.85f, 193.74f)
        cubicTo(198.77f, 193.81f, 198.71f, 193.87f, 198.63f, 193.94f)
        cubicTo(198.29f, 194.26f, 197.96f, 194.57f, 197.62f, 194.89f)
        cubicTo(197.61f, 194.9f, 197.6f, 194.91f, 197.59f, 194.92f)
        cubicTo(197.13f, 195.36f, 196.68f, 195.8f, 196.24f, 196.24f)
    }

    private fun getPath3(): Path = Path().apply {
        moveTo(419.41f, 419.41f)
        cubicTo(357.39f, 423.37f, 277.73f, 444.95f, 204.53f, 477.19f)
        lineTo(204.54f, 477.2f)
        cubicTo(233.5f, 551.75f, 274.55f, 623.35f, 315.61f, 670f)
        cubicTo(320.12f, 664.87f, 324.63f, 660.05f, 329.12f, 655.55f)
        cubicTo(373.37f, 611.31f, 427.35f, 594.02f, 480.45f, 594.02f)
        cubicTo(482.55f, 594.02f, 484.66f, 594.04f, 486.75f, 594.09f)
        cubicTo(446.79f, 556.12f, 418.75f, 504.23f, 418.75f, 439.18f)
        cubicTo(418.75f, 432.81f, 418.98f, 426.22f, 419.41f, 419.41f)
        moveTo(477.21f, 204.53f)
        cubicTo(477.21f, 204.54f, 477.21f, 204.54f, 477.21f, 204.54f)
        cubicTo(477.2f, 204.54f, 477.2f, 204.54f, 477.19f, 204.54f)
        cubicTo(444.95f, 277.73f, 423.36f, 357.38f, 419.41f, 419.4f)
        cubicTo(426.22f, 418.97f, 432.82f, 418.75f, 439.18f, 418.75f)
        cubicTo(504.23f, 418.75f, 556.12f, 446.79f, 594.1f, 486.75f)
        cubicTo(594.04f, 484.65f, 594.01f, 482.55f, 594.01f, 480.45f)
        cubicTo(594.01f, 427.36f, 611.31f, 373.37f, 655.56f, 329.12f)
        cubicTo(660.05f, 324.63f, 664.87f, 320.12f, 670f, 315.61f)
        cubicTo(623.35f, 274.55f, 551.76f, 233.49f, 477.21f, 204.53f)
        moveTo(1024.39f, 670f)
        lineTo(1024.39f, 670f)
        cubicTo(1019.88f, 675.13f, 1015.37f, 679.94f, 1010.87f, 684.45f)
        cubicTo(966.62f, 728.69f, 912.65f, 745.98f, 859.55f, 745.98f)
        cubicTo(857.45f, 745.98f, 855.34f, 745.96f, 853.24f, 745.9f)
        cubicTo(893.21f, 783.87f, 921.25f, 835.77f, 921.25f, 900.82f)
        cubicTo(921.25f, 907.18f, 921.02f, 913.77f, 920.59f, 920.58f)
        cubicTo(982.61f, 916.63f, 1062.26f, 895.03f, 1135.46f, 862.8f)
        lineTo(1135.46f, 862.8f)
        cubicTo(1135.46f, 862.79f, 1135.46f, 862.79f, 1135.47f, 862.79f)
        cubicTo(1106.5f, 788.24f, 1065.44f, 716.65f, 1024.39f, 670f)
        moveTo(745.9f, 853.24f)
        cubicTo(745.95f, 855.34f, 745.99f, 857.44f, 745.99f, 859.55f)
        cubicTo(745.98f, 912.65f, 728.68f, 966.63f, 684.44f, 1010.88f)
        cubicTo(679.94f, 1015.37f, 675.12f, 1019.88f, 670f, 1024.39f)
        cubicTo(716.65f, 1065.44f, 788.24f, 1106.5f, 862.79f, 1135.46f)
        lineTo(862.79f, 1135.45f)
        cubicTo(862.8f, 1135.45f, 862.79f, 1135.45f, 862.8f, 1135.45f)
        cubicTo(895.05f, 1062.26f, 916.63f, 982.61f, 920.59f, 920.58f)
        cubicTo(913.77f, 921.02f, 907.17f, 921.25f, 900.82f, 921.25f)
        cubicTo(835.77f, 921.25f, 783.88f, 893.21f, 745.9f, 853.24f)
        moveTo(315.61f, 670f)
        lineTo(315.61f, 670f)
        cubicTo(274.55f, 716.66f, 233.5f, 788.24f, 204.53f, 862.79f)
        cubicTo(204.54f, 862.8f, 204.53f, 862.79f, 204.54f, 862.8f)
        cubicTo(204.54f, 862.8f, 204.54f, 862.8f, 204.54f, 862.81f)
        cubicTo(277.74f, 895.05f, 357.39f, 916.63f, 419.41f, 920.59f)
        cubicTo(418.98f, 913.77f, 418.75f, 907.18f, 418.75f, 900.82f)
        cubicTo(418.75f, 835.77f, 446.79f, 783.87f, 486.76f, 745.91f)
        cubicTo(484.65f, 745.96f, 482.55f, 745.98f, 480.45f, 745.98f)
        cubicTo(427.35f, 745.98f, 373.38f, 728.69f, 329.12f, 684.44f)
        cubicTo(324.62f, 679.95f, 320.12f, 675.13f, 315.61f, 670f)
        moveTo(594.1f, 853.24f)
        cubicTo(556.12f, 893.21f, 504.22f, 921.25f, 439.17f, 921.25f)
        cubicTo(432.82f, 921.25f, 426.22f, 921.02f, 419.41f, 920.59f)
        cubicTo(423.37f, 982.61f, 444.95f, 1062.26f, 477.19f, 1135.45f)
        lineTo(477.2f, 1135.45f)
        cubicTo(477.2f, 1135.46f, 477.2f, 1135.45f, 477.2f, 1135.46f)
        cubicTo(551.75f, 1106.5f, 623.34f, 1065.44f, 670f, 1024.39f)
        cubicTo(664.87f, 1019.88f, 660.05f, 1015.37f, 655.55f, 1010.88f)
        cubicTo(611.31f, 966.63f, 594.02f, 912.65f, 594.01f, 859.55f)
        cubicTo(594.01f, 857.44f, 594.05f, 855.34f, 594.1f, 853.24f)
        moveTo(920.59f, 419.41f)
        cubicTo(921.02f, 426.22f, 921.25f, 432.81f, 921.25f, 439.18f)
        cubicTo(921.25f, 504.23f, 893.21f, 556.12f, 853.25f, 594.09f)
        cubicTo(855.34f, 594.04f, 857.45f, 594.02f, 859.55f, 594.02f)
        cubicTo(912.65f, 594.02f, 966.63f, 611.31f, 1010.88f, 655.55f)
        cubicTo(1015.37f, 660.05f, 1019.88f, 664.87f, 1024.39f, 670f)
        cubicTo(1065.44f, 623.35f, 1106.5f, 551.75f, 1135.46f, 477.21f)
        cubicTo(1135.45f, 477.2f, 1135.46f, 477.21f, 1135.45f, 477.2f)
        cubicTo(1135.45f, 477.19f, 1135.45f, 477.2f, 1135.45f, 477.19f)
        cubicTo(1062.26f, 444.95f, 982.61f, 423.37f, 920.59f, 419.4f)
        moveTo(862.79f, 204.53f)
        cubicTo(788.24f, 233.49f, 716.65f, 274.55f, 669.99f, 315.61f)
        cubicTo(675.13f, 320.12f, 679.94f, 324.63f, 684.44f, 329.12f)
        cubicTo(728.69f, 373.37f, 745.98f, 427.35f, 745.98f, 480.45f)
        cubicTo(745.98f, 482.55f, 745.96f, 484.65f, 745.9f, 486.75f)
        cubicTo(783.87f, 446.79f, 835.77f, 418.75f, 900.82f, 418.75f)
        cubicTo(907.18f, 418.75f, 913.78f, 418.97f, 920.58f, 419.4f)
        cubicTo(916.63f, 357.38f, 895.04f, 277.73f, 862.81f, 204.54f)
        cubicTo(862.8f, 204.54f, 862.8f, 204.54f, 862.79f, 204.54f)
        cubicTo(862.79f, 204.54f, 862.79f, 204.54f, 862.79f, 204.53f)
    }

    private fun getPath4(): Path = Path().apply {
        moveTo(439.17f, 418.75f)
        cubicTo(432.82f, 418.75f, 426.23f, 418.98f, 419.41f, 419.41f)
        cubicTo(418.98f, 426.22f, 418.75f, 432.82f, 418.75f, 439.17f)
        cubicTo(418.75f, 504.23f, 446.79f, 556.12f, 486.76f, 594.1f)
        cubicTo(559.37f, 595.94f, 629.52f, 629.52f, 670f, 670f)
        cubicTo(629.52f, 629.52f, 595.95f, 559.35f, 594.1f, 486.75f)
        cubicTo(556.12f, 446.79f, 504.22f, 418.75f, 439.17f, 418.75f)
        moveTo(670f, 670f)
        cubicTo(707.99f, 707.99f, 739.89f, 772.12f, 745.2f, 839.87f)
        cubicTo(745.55f, 844.32f, 745.79f, 848.78f, 745.9f, 853.25f)
        cubicTo(783.87f, 893.21f, 835.77f, 921.24f, 900.82f, 921.24f)
        cubicTo(907.18f, 921.24f, 913.78f, 921.02f, 920.58f, 920.58f)
        cubicTo(921.02f, 913.77f, 921.24f, 907.18f, 921.24f, 900.82f)
        cubicTo(921.24f, 835.77f, 893.21f, 783.87f, 853.25f, 745.9f)
        cubicTo(780.64f, 744.05f, 710.47f, 710.47f, 670f, 670f)
        moveTo(670f, 670f)
        cubicTo(629.52f, 710.47f, 559.35f, 744.05f, 486.75f, 745.9f)
        cubicTo(446.79f, 783.87f, 418.75f, 835.77f, 418.75f, 900.82f)
        cubicTo(418.75f, 907.18f, 418.98f, 913.77f, 419.41f, 920.59f)
        cubicTo(426.22f, 921.02f, 432.82f, 921.24f, 439.17f, 921.24f)
        cubicTo(504.22f, 921.24f, 556.12f, 893.21f, 594.1f, 853.25f)
        cubicTo(595.95f, 780.64f, 629.53f, 710.47f, 670f, 670f)
        moveTo(900.82f, 418.75f)
        cubicTo(835.77f, 418.75f, 783.87f, 446.79f, 745.9f, 486.75f)
        cubicTo(745.78f, 491.22f, 745.55f, 495.68f, 745.2f, 500.12f)
        cubicTo(739.9f, 567.68f, 708.17f, 631.64f, 670.33f, 669.67f)
        cubicTo(706.58f, 633.61f, 766.36f, 603.08f, 830.38f, 595.71f)
        cubicTo(837.96f, 594.83f, 845.59f, 594.29f, 853.25f, 594.1f)
        cubicTo(893.21f, 556.12f, 921.24f, 504.23f, 921.24f, 439.17f)
        cubicTo(921.24f, 432.82f, 921.03f, 426.22f, 920.58f, 419.41f)
        cubicTo(913.78f, 418.98f, 907.18f, 418.75f, 900.82f, 418.75f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
    }

    private fun getPath5(): Path = Path().apply {
        moveTo(480.45f, 594.01f)
        cubicTo(427.35f, 594.02f, 373.37f, 611.31f, 329.12f, 655.56f)
        cubicTo(324.63f, 660.05f, 320.12f, 664.87f, 315.61f, 670f)
        cubicTo(320.12f, 675.12f, 324.63f, 679.94f, 329.12f, 684.44f)
        cubicTo(373.37f, 728.69f, 427.35f, 745.98f, 480.45f, 745.98f)
        cubicTo(482.55f, 745.98f, 484.65f, 745.96f, 486.75f, 745.9f)
        cubicTo(539.4f, 695.87f, 612.75f, 670f, 670f, 670f)
        cubicTo(612.75f, 670f, 539.4f, 644.12f, 486.75f, 594.1f)
        cubicTo(484.66f, 594.04f, 482.55f, 594.01f, 480.45f, 594.01f)
        moveTo(670f, 315.61f)
        cubicTo(664.87f, 320.12f, 660.05f, 324.63f, 655.56f, 329.12f)
        cubicTo(611.31f, 373.37f, 594.02f, 427.35f, 594.02f, 480.45f)
        cubicTo(594.02f, 482.56f, 594.04f, 484.65f, 594.09f, 486.75f)
        cubicTo(643.96f, 539.23f, 669.83f, 612.28f, 670f, 669.44f)
        cubicTo(670.15f, 617.04f, 691.91f, 551.29f, 733.94f, 500.27f)
        cubicTo(737.76f, 495.64f, 741.75f, 491.12f, 745.9f, 486.75f)
        cubicTo(745.95f, 484.65f, 745.99f, 482.56f, 745.99f, 480.45f)
        cubicTo(745.98f, 427.35f, 728.68f, 373.37f, 684.44f, 329.12f)
        cubicTo(679.94f, 324.63f, 675.12f, 320.12f, 670f, 315.61f)
        moveTo(670f, 670f)
        cubicTo(670f, 727.24f, 644.12f, 800.6f, 594.1f, 853.25f)
        cubicTo(594.04f, 855.33f, 594.01f, 857.44f, 594.01f, 859.54f)
        cubicTo(594.02f, 912.65f, 611.31f, 966.62f, 655.55f, 1010.87f)
        cubicTo(660.05f, 1015.37f, 664.87f, 1019.87f, 670f, 1024.38f)
        cubicTo(675.13f, 1019.87f, 679.94f, 1015.37f, 684.44f, 1010.87f)
        cubicTo(728.69f, 966.62f, 745.98f, 912.65f, 745.98f, 859.54f)
        cubicTo(745.98f, 857.44f, 745.95f, 855.33f, 745.9f, 853.25f)
        cubicTo(695.88f, 800.6f, 670f, 727.24f, 670f, 670f)
        moveTo(859.55f, 594.01f)
        cubicTo(857.45f, 594.01f, 855.34f, 594.04f, 853.24f, 594.1f)
        cubicTo(848.82f, 598.3f, 844.25f, 602.33f, 839.56f, 606.19f)
        cubicTo(788.39f, 648.29f, 722.43f, 670f, 670f, 670f)
        cubicTo(721.76f, 670f, 786.7f, 691.15f, 837.59f, 732.21f)
        cubicTo(842.98f, 736.54f, 848.2f, 741.11f, 853.24f, 745.9f)
        cubicTo(855.34f, 745.96f, 857.45f, 745.99f, 859.55f, 745.99f)
        cubicTo(912.65f, 745.99f, 966.62f, 728.69f, 1010.87f, 684.44f)
        cubicTo(1015.37f, 679.95f, 1019.88f, 675.13f, 1024.39f, 670f)
        cubicTo(1019.88f, 664.87f, 1015.37f, 660.05f, 1010.87f, 655.56f)
        cubicTo(966.63f, 611.31f, 912.65f, 594.01f, 859.55f, 594.01f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
    }

    private fun getPath6(): Path = Path().apply {
        moveTo(670f, 670f)
        cubicTo(612.75f, 670f, 539.4f, 695.87f, 486.75f, 745.9f)
        cubicTo(559.35f, 744.05f, 629.52f, 710.47f, 670f, 670f)
        moveTo(486.75f, 594.1f)
        cubicTo(539.4f, 644.12f, 612.76f, 670f, 670f, 670f)
        cubicTo(629.52f, 629.52f, 559.36f, 595.94f, 486.75f, 594.1f)
        moveTo(594.1f, 486.75f)
        cubicTo(595.95f, 559.35f, 629.53f, 629.52f, 670f, 670f)
        cubicTo(670f, 612.76f, 644.12f, 539.4f, 594.1f, 486.75f)
        moveTo(670f, 669.44f)
        lineTo(670f, 670f)
        lineTo(670f, 669.44f)
        moveTo(670f, 670f)
        lineTo(670f, 670f)
        moveTo(745.9f, 486.75f)
        cubicTo(741.75f, 491.12f, 737.76f, 495.64f, 733.94f, 500.27f)
        cubicTo(691.75f, 551.46f, 670f, 617.5f, 670f, 670f)
        cubicTo(707.98f, 632.01f, 739.89f, 567.89f, 745.21f, 500.12f)
        cubicTo(745.55f, 495.68f, 745.79f, 491.22f, 745.9f, 486.75f)
        moveTo(670.33f, 669.67f)
        cubicTo(670.22f, 669.78f, 670.11f, 669.89f, 670f, 670f)
        cubicTo(670.11f, 669.89f, 670.22f, 669.78f, 670.33f, 669.67f)
        moveTo(670f, 670f)
        cubicTo(629.53f, 710.47f, 595.95f, 780.64f, 594.1f, 853.25f)
        cubicTo(644.12f, 800.6f, 670f, 727.24f, 670f, 670f)
        moveTo(670f, 670f)
        cubicTo(670f, 727.24f, 695.88f, 800.6f, 745.9f, 853.25f)
        cubicTo(745.79f, 848.78f, 745.55f, 844.32f, 745.2f, 839.87f)
        cubicTo(739.89f, 772.12f, 707.99f, 707.99f, 670f, 670f)
        moveTo(670f, 670f)
        cubicTo(710.47f, 710.47f, 780.64f, 744.05f, 853.25f, 745.9f)
        cubicTo(848.2f, 741.11f, 842.97f, 736.54f, 837.6f, 732.2f)
        cubicTo(786.7f, 691.16f, 721.77f, 670f, 670f, 670f)
        moveTo(853.25f, 594.1f)
        cubicTo(845.59f, 594.29f, 837.96f, 594.84f, 830.38f, 595.71f)
        cubicTo(766.17f, 603.1f, 706.21f, 633.79f, 670f, 670f)
        cubicTo(722.43f, 670f, 788.38f, 648.29f, 839.56f, 606.19f)
        cubicTo(844.26f, 602.33f, 848.82f, 598.3f, 853.25f, 594.1f)
    }
}
