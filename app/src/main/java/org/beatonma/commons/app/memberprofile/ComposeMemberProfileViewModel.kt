package org.beatonma.commons.app.memberprofile

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.SavedStateKey
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.core.interfaces.compressConsecutiveItems
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.get
import org.beatonma.commons.data.set
import org.beatonma.commons.repo.repository.MemberRepository

private val MemberIdKey = SavedStateKey("member_id")

class ComposeMemberProfileViewModel
@ViewModelInject constructor(
    private val memberRepository: MemberRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var memberID: ParliamentID
        set(value) {
            savedStateHandle[MemberIdKey] = value
        }
        get() = savedStateHandle[MemberIdKey] ?: error("Member ID has not been set")

    fun getMemberData() = memberRepository.getMember(memberID)

    suspend fun constructHistoryOf(member: CompleteMember): List<Temporal> {
        fun <T : Temporal> MutableList<Temporal>.addEvents(items: List<T>?) {
            withNotNull(items) { safeItems ->
                addAll(
                    safeItems.mapNotNull {
                        try {
                            it
                        }
                        catch (e: Exception) {
                            null
                        }
                    }
                )
            }
        }

        return mutableListOf<Temporal>().apply {
            addEvents(member.posts)
            addEvents(member.houses)
            addEvents(member.experiences)
            addEvents(compressConstituencies(member.historicConstituencies))
            addEvents(compressParties(member.parties))
            addEvents(member.committees)
            addEvents(member.financialInterests)

            sortBy { it.startOf() }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun compressParties(parties: List<PartyAssociationWithParty>?) =
        compressConsecutiveItems(
            parties,
            areItemsTheSame = { a, b -> a.party == b.party },
            combineFunc = { a, b ->
                a.copy(
                    partyAssocation = a.partyAssocation.copy(
                        end = b.end
                    )
                )
            }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun compressConstituencies(constituencies: List<HistoricalConstituencyWithElection>?) =
        compressConsecutiveItems(
            constituencies,
            areItemsTheSame = { a, b ->
                a.constituency == b.constituency
            },
            combineFunc = { a, b ->
                a.copy(
                    historicalConstituency = a.historicalConstituency.copy(
                        end = b.historicalConstituency.end
                    )
                )
            }
        )
}
