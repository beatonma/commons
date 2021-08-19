package org.beatonma.commons.repo.repository

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.resultFlowNoCache
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class SocialRepository @Inject constructor(
    private val remoteSource: CommonsApi,
) {
    /**
     * [SnommocToken] should be provided if the user is signed in so that any existing vote by them
     * can be displayed.
     */
    fun getSocialContent(target: SocialTarget, snommocToken: SnommocToken?) =
        getSocialContent(target.targetType, target.parliamentdotuk, snommocToken)

    /**
     * [UserToken] should be provided if the user is signed in so that any existing vote by them
     * can be displayed.
     */
    fun getSocialContent(target: SocialTarget, userToken: UserToken?) =
        getSocialContent(target.targetType, target.parliamentdotuk, userToken?.snommocToken)

    suspend fun postComment(comment: CreatedComment) = remoteSource.postComment(comment)
    suspend fun postVote(vote: CreatedVote) = remoteSource.postVote(vote)

    private fun getSocialContent(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ) = resultFlowNoCache {
        println("getSocialContent()")
        remoteSource.getSocialForTarget(targetType, parliamentdotuk, snommocToken)
    }
}

