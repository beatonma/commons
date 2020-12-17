package org.beatonma.commons.app.bill

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.beatonma.commons.app
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.extensions.BillStageCanonicalNames
import org.beatonma.commons.data.extensions.startedIn
import org.beatonma.commons.repo.ResultLiveData
import org.beatonma.commons.repo.repository.BillRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.onSuccess
import java.util.*

class BillDetailViewModel @ViewModelInject constructor(
    private val repository: BillRepository,
    @ApplicationContext context: Context,
) : AndroidViewModel(context.app) {

    lateinit var billLiveData: ResultLiveData<CompleteBill>

    private val _stagesLiveData: MutableLiveData<List<AnnotatedBillStage>> = MutableLiveData()
    internal val stagesLiveData: LiveData<List<AnnotatedBillStage>> = _stagesLiveData

    private val stagesObserver = Observer { result: IoResult<CompleteBill> ->
        result.onSuccess { bill ->
            viewModelScope.launch {
                _stagesLiveData.postValue(getAnnotatedStages(bill))
            }
        }
    }

    fun forBill(parliamentdotuk: ParliamentID) {
        billLiveData = repository.getBill(parliamentdotuk).asLiveData()
        billLiveData.observeForever(stagesObserver)
    }

    override fun onCleared() {
        billLiveData.removeObserver(stagesObserver)
        super.onCleared()
    }

    /**
     * Resolve House and BillStageProgress data for each BillStage.
     */
    private suspend fun getAnnotatedStages(completeBill: CompleteBill): List<AnnotatedBillStage> {
        val originatingHouse = completeBill.bill?.startedIn() ?: return listOf()
        val stages = completeBill.stages ?: return listOf()

        return getAnnotatedStages(originatingHouse, stages)
    }
}

internal data class AnnotatedBillStage(
    val stage: BillStageWithSittings,
    val house: House?,
    val progress: BillStageProgress?,
) {

    /**
     * See https://www.parliament.uk/about/how/laws/passage-bill/commons/coms-consideration-of-amendments/
     * Return true if bill is in a stage that belongs to neither particular house - it is being
     * passed back and forth until both houses are in agreement.
     */
    fun isConsiderationOfAmendments(): Boolean = progress == BillStageProgress.ConsiderationOfAmendments
    fun isRoyalAssent(): Boolean = progress == BillStageProgress.RoyalAssent
}

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

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun classifyProgress(stageType: String, previous: BillStageProgress?): BillStageProgress? {
    val stageTypeLowercase = stageType.toLowerCase(Locale.ROOT)

    BillStageProgress.values().filter { it.ordinal >= previous?.ordinal ?: -1 }.forEach {
        if (stageTypeLowercase.startsWith(it.canonicalPrefix)) {
            return it
        }
    }
    return previous
}

/**
 * If null:
 * - the Bill may be going through Consideration of Amendments which may involve going
 *   back and forth between the two Houses, or
 * - the Bill may have reached Royal Assent, in which case the process is finished, or
 * - potentially, the Bill may not have even had its first reading in its originating House? I suspect
 *   such a Bill would not be present in the database yet but it is not impossible.
 */
internal fun getCurrentHouse(startingHouse: House, progress: BillStageProgress?): House? = when (progress) {
    BillStageProgress.FirstFirstReading,
    BillStageProgress.FirstSecondReading,
    BillStageProgress.FirstThirdReading,
    BillStageProgress.FirstCommitteeStage,
    BillStageProgress.FirstReportStage,
    -> startingHouse

    BillStageProgress.SecondFirstReading,
    BillStageProgress.SecondSecondReading,
    BillStageProgress.SecondThirdReading,
    BillStageProgress.SecondCommitteeStage,
    BillStageProgress.SecondReportStage,
    -> startingHouse.otherPlace()

    else -> null
}


@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal suspend fun getAnnotatedStages(originatingHouse: House, stagesWithSittings: List<BillStageWithSittings>): List<AnnotatedBillStage> {
    val orderedStages = stagesWithSittings.sortedBy { stage ->
        stage.sittings.maxByOrNull { sitting -> sitting.date }?.date
    }

    var progress: BillStageProgress? = null
    return orderedStages.map { stageWithSittings ->
        val type = stageWithSittings.stage.type
        progress = classifyProgress(type, previous = progress)
        AnnotatedBillStage(stageWithSittings, getCurrentHouse(originatingHouse, progress), progress)
    }
}
