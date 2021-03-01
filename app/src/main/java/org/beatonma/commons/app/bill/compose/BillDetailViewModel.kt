package org.beatonma.commons.app.bill.compose

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.app.bill.compose.viewmodel.AnnotatedBillStage
import org.beatonma.commons.app.bill.compose.viewmodel.BillStageProgress
import org.beatonma.commons.app.bill.compose.viewmodel.classifyProgress
import org.beatonma.commons.app.bill.compose.viewmodel.getCategory
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.SavedStateKey
import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.extensions.startedIn
import org.beatonma.commons.data.get
import org.beatonma.commons.data.set
import org.beatonma.commons.repo.repository.BillRepository
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject

private val BillKey = SavedStateKey("bill_id")

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class BillDetailViewModel @Inject constructor(
    private val repository: BillRepository,
    private val savedStateHandle: SavedStateHandle,
) : IoLiveDataViewModel<CompleteBill>(), SocialTargetProvider {

    val billID: ParliamentID get() = savedStateHandle[BillKey]!!

    override val socialTarget: SocialTarget
        get() = SocialTarget(SocialTargetType.bill, billID)

    init {
        forSavedBill()
    }

    private fun forSavedBill() {
        val billId: ParliamentID? = savedStateHandle[BillKey]
        if (billId != null) {
            forBill(billId)
        }
    }

    fun forBill(billId: ParliamentID) {
        savedStateHandle[BillKey] = billId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getBill(billId).collectLatest { result -> postValue(result) }
        }
    }
}

/**
 * Resolve House and BillStageProgress data for each BillStage.
 */
internal fun CompleteBill.getAnnotatedBillStages(): List<AnnotatedBillStage> {
    val originatingHouse = bill.startedIn()

    return getAnnotatedStages(originatingHouse, stages)
}


@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun getAnnotatedStages(
    originatingHouse: House,
    stagesWithSittings: List<BillStageWithSittings>
): List<AnnotatedBillStage> {
    val orderedStages = stagesWithSittings.sortedBy { stage ->
        stage.sittings.maxByOrNull { sitting -> sitting.date }?.date
    }

    var progress: BillStageProgress = BillStageProgress.FirstFirstReading
    return orderedStages.map { stageWithSittings ->
        val type = stageWithSittings.stage.type
        progress = classifyProgress(type, previous = progress)
        AnnotatedBillStage(
            stageWithSittings,
            progress.getCategory(originatingHouse),
            progress)
    }
}
