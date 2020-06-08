package org.beatonma.commons.data.core.repository

import android.os.Bundle
import androidx.core.os.bundleOf
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.social.CreatedComment
import org.beatonma.commons.data.core.social.CreatedVote
import org.beatonma.commons.data.core.social.SocialTargetType
import org.beatonma.commons.data.resultLiveDataNoCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
) {
    private fun observeSocialContent(targetType: SocialTargetType, parliamentdotuk: ParliamentID) =
        resultLiveDataNoCache { remoteSource.getSocialForTarget(targetType, parliamentdotuk) }

    fun observeSocialContent(target: Sociable) =
        observeSocialContent(target.getSocialContentTarget(), target.parliamentdotuk)

    fun observeSocialContent(target: SocialTarget) =
        observeSocialContent(target.targetType, target.parliamentdotuk)

    fun observeMemberContent(parliamentdotuk: ParliamentID) =
        observeSocialContent(SocialTargetType.member, parliamentdotuk)

    fun observeBillContent(parliamentdotuk: ParliamentID) =
        observeSocialContent(SocialTargetType.bill, parliamentdotuk)

    fun observeCommonsDivisionContent(parliamentdotuk: ParliamentID) =
        observeSocialContent(SocialTargetType.division_commons, parliamentdotuk)

    fun observeLordsDivisionContent(parliamentdotuk: ParliamentID) =
        observeSocialContent(SocialTargetType.division_lords, parliamentdotuk)


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
