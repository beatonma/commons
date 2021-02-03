package org.beatonma.commons.repo.remotesource.api

import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.ApiBill
import org.beatonma.commons.snommoc.models.ApiCompleteMember
import org.beatonma.commons.snommoc.models.ApiConstituency
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import org.beatonma.commons.snommoc.models.ApiDivision
import org.beatonma.commons.snommoc.models.ApiMemberVote
import org.beatonma.commons.snommoc.models.ApiZeitgeist
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.social.ApiUserName
import org.beatonma.commons.snommoc.models.social.ApiUserToken
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTargetType

interface CommonsApi {
    // READ
    suspend fun getMember(parliamentdotuk: ParliamentID): IoResult<ApiCompleteMember>

    suspend fun getBill(parliamentdotuk: ParliamentID): IoResult<ApiBill>

    suspend fun getVotesForMember(
        house: House,
        parliamentdotuk: ParliamentID,
    ): IoResult<List<ApiMemberVote>>

    suspend fun getDivision(
        house: House,
        parliamentdotuk: ParliamentID,
    ): IoResult<ApiDivision>

    suspend fun getConstituency(parliamentdotuk: ParliamentID): IoResult<ApiConstituency>
    suspend fun getConstituencyDetailsForElection(
        constituencyId: Int,
        electionId: Int,
    ): IoResult<ApiConstituencyElectionDetails>

    suspend fun getSearchResults(query: String): IoResult<List<MemberSearchResult>>

    suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ): IoResult<SocialContent>

    suspend fun getUsername(userToken: UserToken): IoResult<ApiUserName>

    suspend fun getZeitgeist(): IoResult<ApiZeitgeist>

    // WRITE
    suspend fun registerUser(googleToken: String): IoResult<ApiUserToken>

    suspend fun requestRenameAccount(userToken: UserToken, newName: String): IoResult<Void>

    suspend fun deleteUserAccount(token: UserToken): IoResult<Void>

    // Social
    @SignInRequired
    suspend fun postComment(comment: CreatedComment): IoResult<Void>

    @SignInRequired
    suspend fun postVote(vote: CreatedVote): IoResult<Void>
}
