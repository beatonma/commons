package org.beatonma.commons.data.core

import androidx.room.Embedded
import org.beatonma.commons.data.core.room.entities.MemberProfile


data class Member(
    @Embedded val profile: MemberProfile
)
