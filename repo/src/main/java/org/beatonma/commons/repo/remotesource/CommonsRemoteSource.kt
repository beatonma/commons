package org.beatonma.commons.repo.remotesource

import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.social.DeleteUserRequest
import org.beatonma.commons.snommoc.models.social.DeletedVote
import org.beatonma.commons.snommoc.models.social.RenameAccountRequest
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import javax.inject.Inject

class CommonsRemoteSource @Inject constructor(
    private val service: CommonsService,
) : RemoteSource, CommonsApi {

    override suspend fun getZeitgeist() = getResult(service::getZeitgeist)

    override suspend fun getMember(parliamentdotuk: ParliamentID) = getResult {
        service.getMember(parliamentdotuk)
    }

    override suspend fun getBill(parliamentdotuk: ParliamentID) = getResult {
        service.getBill(parliamentdotuk)
    }

    override suspend fun getVotesForMember(house: House, parliamentdotuk: ParliamentID) = getResult {
        service.getVotesForMember(house, parliamentdotuk)
    }

    override suspend fun getDivision(house: House, parliamentdotuk: ParliamentID) = getResult {
        service.getDivision(house, parliamentdotuk)
    }

    override suspend fun getConstituency(parliamentdotuk: ParliamentID) = getResult {
        service.getConstituency(parliamentdotuk)
    }

    override suspend fun getConstituencyDetailsForElection(constituencyId: Int, electionId: Int) =
        getResult {
            service.getConstituencyElectionResults(constituencyId, electionId)
        }

    override suspend fun getSearchResults(query: String) = getResult {
        service.getSearchResults(query)
    }

    override suspend fun getUsername(userToken: UserToken) = getResult {
        service.getUsername(userToken.snommocToken)
    }

    override suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ) = getResult {
        service.getSocialContentForTarget(targetType.name, parliamentdotuk, snommocToken)
    }

    // Write

    override suspend fun registerUser(googleToken: String) = getResult {
        service.registerGoogleSignIn(googleToken)
    }

    override suspend fun requestRenameAccount(
        userToken: UserToken,
        newName: String
    ): IoResult<Void> = getResult {
        service.requestRenameAccount(RenameAccountRequest(
            currentUsername = userToken.username,
            token = userToken.snommocToken,
            newUsername = newName
        ))
    }

    override suspend fun deleteUserAccount(token: UserToken): IoResult<Void> = getResult {
        service.deleteUserAccount(DeleteUserRequest(
            gtoken = token.googleId,
            token = token.snommocToken
        ))
    }


    @SignInRequired
    override suspend fun postComment(comment: CreatedComment) = getResult {
        service.postComment(
            comment.target.targetType.name,
            comment.target.parliamentdotuk,
            comment.userToken.snommocToken,
            comment.text
        )
    }

    @SignInRequired
    override suspend fun postVote(vote: CreatedVote) = getResult {
        if (vote.voteType == SocialVoteType.NULL) {
            service.deleteVote(
                vote.target.targetType.name,
                vote.target.parliamentdotuk,
                DeletedVote(vote.userToken.snommocToken)
            )
        }
        else {
            service.postVote(
                vote.target.targetType.name,
                vote.target.parliamentdotuk,
                vote.userToken.snommocToken,
                vote.voteType.apiName,
            )
        }
    }
}
