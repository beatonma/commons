package org.beatonma.commons.app.memberprofile.compose

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app
import org.beatonma.commons.app.signin.BaseUserAccountViewModel
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.core.interfaces.compressConsecutiveItems
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.repo.repository.MemberRepository
import org.beatonma.commons.repo.repository.UserRepository

class ComposeMemberProfileViewModel
@ViewModelInject constructor(
    private val memberRepository: MemberRepository,
    userRepository: UserRepository,
    @ApplicationContext context: Context,
) : BaseUserAccountViewModel(userRepository, context.app) {

    fun forMember(parliamentID: ParliamentID) =
        memberRepository.getMember(parliamentID)

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
