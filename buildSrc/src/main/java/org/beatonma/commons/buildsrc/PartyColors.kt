package org.beatonma.commons.buildsrc

private fun String.toColorInt(): Int {
    // Use a long to avoid rollovers on #ffXXXXXX
    var color = this.substring(1).toLong(16)
    if (this.length == 7) {
        // Set the alpha value
        color = color or -0x1000000
    } else require(this.length == 9) { "Unknown color: $this" }
    return color.toInt()
}

//const val TEXT_DARK = 1
//const val TEXT_LIGHT = 2

private val RED: String = "#F44336"
private val AMBER: String = "#FFC107"
private val BLUE: String = "#2196F3"


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
    val primaryText: Int = TEXT_LIGHT,
    val accentText: Int = TEXT_LIGHT
) {
    val _primaryInt: Int get() = primary.toColorInt()
    val _accentInt: Int get() = accent.toColorInt()

    companion object {
        const val TEXT_DARK = 1
        const val TEXT_LIGHT = 2
    }
}

object DefaultColors : PartyColors(
    primary = "#6f6f6f",
    accent = "#E57373",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object AllianceColors : PartyColors(
    primary = "#F4C72E",
    accent = "#000000",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object ChangeUkColors : PartyColors(
    primary = "#ffffff",
    accent = "#222221",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object ConservativeColors : PartyColors(
    primary = "#11437D",
    accent = RED,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object DupColors : PartyColors(
    primary = "#343067",
    accent = RED,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object GreenColors : PartyColors(
    primary = "#538C6B",
    accent = "#8BC34A",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object LabourColors : PartyColors(
    primary = "#C41230",
    accent = AMBER,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object LabourCoopColors : PartyColors(
    primary = "#711F8E",
    accent = RED,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object LibDemColors : PartyColors(
    primary = "#FDBB30",
    accent = BLUE,
    primaryText = TEXT_DARK,
    accentText = TEXT_LIGHT
)

object PlaidCymruColors : PartyColors(
    primary = "#008142",
    accent = AMBER,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object RespectColors : PartyColors(
    primary = "#9B0103",
    accent = "#028C3E",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object SdlpColors : PartyColors(
    primary = "#0B694D",
    accent = AMBER,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object SinnFeinColors : PartyColors(
    primary = "#086723",
    accent = "#FF5722",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object SnpColors : PartyColors(
    primary = "#FFF95D",
    accent = "#333333",
    primaryText = TEXT_DARK,
    accentText = TEXT_LIGHT
)

object SdpColors : PartyColors(
    primary = "#004377",
    accent = "#CA3349",
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object UkipColors : PartyColors(
    primary = "#702F85",
    accent = AMBER,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object UupColors : PartyColors(
    primary = "#1D2F5D",
    accent = BLUE,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)

object SpeakerColors : PartyColors(
    primary = "#37712C",
    accent = BLUE,
    primaryText = TEXT_LIGHT,
    accentText = TEXT_LIGHT
)
