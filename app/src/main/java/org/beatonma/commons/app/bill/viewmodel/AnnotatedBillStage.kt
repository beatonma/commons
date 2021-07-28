package org.beatonma.commons.app.bill.viewmodel

import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings

internal data class AnnotatedBillStage(
    val stage: BillStageWithSittings,
    val category: BillStageCategory,
    val progress: BillStageProgress,
)
