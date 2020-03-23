import annotation.HexColor

private fun String.toColorInt(): Int {
    // Use a long to avoid rollovers on #ffXXXXXX
    var color = this.substring(1).toLong(16)
    if (this.length == 7) {
        // Set the alpha value
        color = color or -0x1000000
    } else require(this.length == 9) { "Unknown color: $this" }
    return color.toInt()
}

private val DARK_PRIMARY: @HexColor String = "#D8000000"
private val DARK_SECONDARY: @HexColor String = "#89000000"
private val LIGHT_PRIMARY: @HexColor String = "#D8FFFFFF"
private val LIGHT_SECONDARY: @HexColor String = "#89FFFFFF"

private val RED: @HexColor String = "#F44336"
private val AMBER: @HexColor String = "#FFC107"
private val BLUE: @HexColor String = "#2196F3"


val AllPartyThemes = mapOf(
    "default" to DefaultColors,
    "alliance" to AllianceColors,
    "changeuk" to ChangeUkColors,
    "conservative" to ConservativeColors,
    "dup" to DupColors,
    "green" to GreenColors,
    "labour" to LabourColors,
    "labourcoop" to LabourCoopColors,
    "libdem" to LibDemColors,
    "plaidcymru" to PlaidCymruColors,
    "respect" to RespectColors,
    "sdlp" to SdlpColors,
    "sdp" to SdpColors,
    "sinnfein" to SinnFeinColors,
    "snp" to SnpColors,
    "ukip" to UkipColors,
    "uup" to UupColors,
    "speaker" to SpeakerColors
)

abstract class PartyColors(
    val primary: String,
    val accent: String,
    val primaryText: String = LIGHT_PRIMARY,
    val accentText: String = LIGHT_PRIMARY
) {
    val _primaryInt: Int get() = primary.toColorInt()
    val _accentInt: Int get() = accent.toColorInt()
    val _primaryTextInt: Int get() = primaryText.toColorInt()
    val _accentTextInt: Int get() = accentText.toColorInt()
}

object DefaultColors : PartyColors(
    primary = "#6f6f6f",
    accent = "#E57373",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object AllianceColors : PartyColors(
    primary = "#F4C72E",
    accent = "#000000",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object ChangeUkColors : PartyColors(
    primary = "#ffffff",
    accent = "#222221",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object ConservativeColors : PartyColors(
    primary = "#11437D",
    accent = RED,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object DupColors : PartyColors(
    primary = "#343067",
    accent = RED,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object GreenColors : PartyColors(
    primary = "#538C6B",
    accent = "#8BC34A",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object LabourColors : PartyColors(
    primary = "#C41230",
    accent = AMBER,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object LabourCoopColors : PartyColors(
    primary = "#711F8E",
    accent = RED,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object LibDemColors : PartyColors(
    primary = "#FDBB30",
    accent = BLUE,
    primaryText = DARK_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object PlaidCymruColors : PartyColors(
    primary = "#008142",
    accent = AMBER,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object RespectColors : PartyColors(
    primary = "#9B0103",
    accent = "#028C3E",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object SdlpColors : PartyColors(
    primary = "#0B694D",
    accent = AMBER,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object SinnFeinColors : PartyColors(
    primary = "#086723",
    accent = "#FF5722",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object SnpColors : PartyColors(
    primary = "#FFF95D",
    accent = "#333333",
    primaryText = DARK_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object SdpColors : PartyColors(
    primary = "#004377",
    accent = "#CA3349",
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object UkipColors : PartyColors(
    primary = "#702F85",
    accent = AMBER,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object UupColors : PartyColors(
    primary = "#1D2F5D",
    accent = BLUE,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)

object SpeakerColors : PartyColors(
    primary = "#37712C",
    accent = BLUE,
    primaryText = LIGHT_PRIMARY,
    accentText = LIGHT_PRIMARY
)
