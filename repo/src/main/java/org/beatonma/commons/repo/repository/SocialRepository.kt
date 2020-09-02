package org.beatonma.commons.repo.repository

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.result.resultFlowNoCache
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton @Suppress("unused")
class SocialRepository @Inject constructor(
    private val remoteSource: CommonsApi,
) {
    private fun getSocialContent(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?
    ) = resultFlowNoCache {
            remoteSource.getSocialForTarget(targetType, parliamentdotuk, snommocToken)
        }

    fun getSocialContent(target: SocialTarget, snommocToken: SnommocToken?) =
        getSocialContent(target.targetType, target.parliamentdotuk, snommocToken)

    suspend fun postComment(comment: CreatedComment) = remoteSource.postComment(comment)
    suspend fun postVote(vote: CreatedVote) = remoteSource.postVote(vote)
}

