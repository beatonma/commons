package org.beatonma.commons.data.core

import androidx.room.Embedded
import com.squareup.moshi.Json
import org.beatonma.commons.data.core.room.entities.ApiAddresses
import org.beatonma.commons.data.core.room.entities.ApiPosts
import org.beatonma.commons.data.core.room.entities.CommitteeMembership
import org.beatonma.commons.data.core.room.entities.MemberProfile

data class Member(
    @Embedded val profile: MemberProfile
)


/**
 * Class for deserialising complete /member/profile/ api response.
 */
data class ApiCompleteMember(
    val profile: MemberProfile,
    @field:Json(name = "address") val addresses: ApiAddresses,
    @field:Json(name = "posts") val posts: ApiPosts,
    @field:Json(name = "committees") val committees: List<CommitteeMembership>
)
