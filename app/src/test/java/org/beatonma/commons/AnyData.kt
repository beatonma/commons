package org.beatonma.commons

import org.beatonma.commons.app.ui.screens.bill.viewmodel.BillStageProgress
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillStageSitting
import java.time.LocalDate
import kotlin.random.Random

val anyID: ParliamentID get() = (1..10000).random()

val anyDate: LocalDate get() = LocalDate.of(
    (1950..2000).random(),
    (1..12).random(),
    (1..28).random()
)

val anyBool: Boolean get() = Random.nextBoolean()


fun anyBillStageType(): String = BillStageProgress.values().random().canonicalPrefix

fun anyBillStage(
    stageType: String = anyBillStageType(),
    billId: ParliamentID = anyID,
    parliamentdotuk: ParliamentID = anyID,
) = BillStage(
    billId = billId,
    parliamentdotuk = parliamentdotuk,
    type = stageType
)

fun anyBillStageSitting(
    billStageId: Int = anyID,
    date: LocalDate = anyDate,
    parliamentdotuk: ParliamentID = anyID,
    isProvsional: Boolean = anyBool,
    isFormal: Boolean = anyBool,
) = BillStageSitting(
    billStageId = billStageId,
    date = date,
    parliamentdotuk = parliamentdotuk,
    isProvisional = isProvsional,
    isFormal = isFormal
)
