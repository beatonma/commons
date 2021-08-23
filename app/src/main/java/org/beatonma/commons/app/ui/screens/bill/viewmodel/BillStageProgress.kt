package org.beatonma.commons.app.ui.screens.bill.viewmodel

import org.beatonma.commons.core.House
import org.beatonma.commons.data.extensions.BillStageCanonicalNames
import java.util.*

/**
 * https://www.parliament.uk/about/how/laws/passage-bill/commons/coms-royal-assent/
 *
 * Loosely, we can consider there to be 12 steps of progress for a Bill, from First Reading through
 * to Royal Assent where it becomes law. There are others (e.g. Motions to suspend/revive) but
 * we will stick to the standard 12 steps for now.
 */
internal enum class BillStageProgress(internal val canonicalPrefix: String) {
    // Steps in the originating House - may be either House!
    FirstFirstReading(BillStageCanonicalNames.FIRST_READING),
    FirstSecondReading(BillStageCanonicalNames.SECOND_READING),
    FirstCommitteeStage(BillStageCanonicalNames.COMMITTEE_STAGE),
    FirstReportStage(BillStageCanonicalNames.REPORT_STAGE),
    FirstThirdReading(BillStageCanonicalNames.THIRD_READING),

    // Steps in the other House, whichever House that is.
    SecondFirstReading(BillStageCanonicalNames.FIRST_READING),
    SecondSecondReading(BillStageCanonicalNames.SECOND_READING),
    SecondCommitteeStage(BillStageCanonicalNames.COMMITTEE_STAGE),
    SecondReportStage(BillStageCanonicalNames.REPORT_STAGE),
    SecondThirdReading(BillStageCanonicalNames.THIRD_READING),

    // Consideration of Amendments: returns to the originating House, and may 'ping-pong' between
    // Houses until both are in agreement
    ConsiderationOfAmendments(BillStageCanonicalNames.CONSIDERATION_OF_AMENDMENTS),

    // Both Houses agree - the bill becomes law.
    RoyalAssent(BillStageCanonicalNames.ROYAL_ASSENT),
    ;
}

/**
 * If null:
 * - the Bill may be going through Consideration of Amendments which may involve going
 *   back and forth between the two Houses, or
 * - the Bill may have reached Royal Assent, in which case the process is finished, or
 * - potentially, the Bill may not have even had its first reading in its originating House? I suspect
 *   such a Bill would not be present in the database yet but it is not impossible.
 */
internal fun BillStageProgress.getCategory(originatingHouse: House): BillStageCategory =
    when (this) {
        BillStageProgress.FirstFirstReading,
        BillStageProgress.FirstSecondReading,
        BillStageProgress.FirstThirdReading,
        BillStageProgress.FirstCommitteeStage,
        BillStageProgress.FirstReportStage,
        -> originatingHouse.asCurrentPlace()

        BillStageProgress.SecondFirstReading,
        BillStageProgress.SecondSecondReading,
        BillStageProgress.SecondThirdReading,
        BillStageProgress.SecondCommitteeStage,
        BillStageProgress.SecondReportStage,
        -> originatingHouse.otherPlace().asCurrentPlace()

        BillStageProgress.ConsiderationOfAmendments -> BillStageCategory.ConsiderationOfAmendments
        BillStageProgress.RoyalAssent -> BillStageCategory.RoyalAssent
    }

internal fun classifyProgress(stageType: String, previous: BillStageProgress): BillStageProgress {
    val stageTypeLowercase = stageType.lowercase(Locale.ROOT)

    BillStageProgress.values()
        .filter { it.ordinal >= previous.ordinal }
        .forEach {
            if (stageTypeLowercase.startsWith(it.canonicalPrefix)) {
                return it
            }
        }
    return previous
}

private fun House.asCurrentPlace() = when(this) {
    House.commons -> BillStageCategory.Commons
    House.lords -> BillStageCategory.Lords
}
