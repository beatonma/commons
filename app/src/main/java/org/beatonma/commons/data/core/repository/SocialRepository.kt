package org.beatonma.commons.data.core.repository

import android.os.Bundle
import androidx.core.os.bundleOf
import org.beatonma.commons.data.*
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.social.CreatedComment
import org.beatonma.commons.data.core.social.CreatedVote
import org.beatonma.commons.data.core.social.SocialTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
) {
    private fun observeSocialContent(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?
    ) =
        resultLiveDataNoCache { remoteSource.getSocialForTarget(targetType, parliamentdotuk, snommocToken) }

    fun observeSocialContent(target: Sociable, snommocToken: SnommocToken?) =
        observeSocialContent(target.getSocialContentTarget(), target.parliamentdotuk, snommocToken)

    fun observeSocialContent(target: SocialTarget, snommocToken: SnommocToken?) =
        observeSocialContent(target.targetType, target.parliamentdotuk, snommocToken)

    fun observeMemberContent(parliamentdotuk: ParliamentID, snommocToken: SnommocToken?) =
        observeSocialContent(SocialTargetType.member, parliamentdotuk, snommocToken)

    fun observeBillContent(parliamentdotuk: ParliamentID, snommocToken: SnommocToken?) =
        observeSocialContent(SocialTargetType.bill, parliamentdotuk, snommocToken)

    fun observeCommonsDivisionContent(parliamentdotuk: ParliamentID, snommocToken: SnommocToken?) =
        observeSocialContent(SocialTargetType.division_commons, parliamentdotuk, snommocToken)

    fun observeLordsDivisionContent(parliamentdotuk: ParliamentID, snommocToken: SnommocToken?) =
        observeSocialContent(SocialTargetType.division_lords, parliamentdotuk, snommocToken)


    suspend fun postComment(comment: CreatedComment) = remoteSource.postComment(comment)
    suspend fun postVote(vote: CreatedVote) = remoteSource.postVote(vote)
}

data class SocialTarget(val targetType: SocialTargetType, val parliamentdotuk: ParliamentID) {
    constructor(target: Sociable): this(target.getSocialContentTarget(), target.parliamentdotuk)

    constructor(bundle: Bundle): this(
        bundle.get("target_type") as SocialTargetType,
        bundle.getInt(PARLIAMENTDOTUK),
    )

    fun toBundle() = bundleOf(
        "target_type" to targetType,
        PARLIAMENTDOTUK to parliamentdotuk
    )
}