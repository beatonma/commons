package org.beatonma.commons.repo

import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType


fun Sociable.getSocialContentTarget(): SocialTargetType = when(this) {
    is MemberProfile -> SocialTargetType.member
    is Bill -> SocialTargetType.bill
    is Division -> when(house) {
        House.commons -> SocialTargetType.division_commons
        House.lords -> SocialTargetType.division_lords
    }
    else -> throw Exception("Unregistered Sociable implementation - getSocialContentTarget()")
}

fun Sociable.asSocialTarget(): SocialTarget =
    SocialTarget(getSocialContentTarget(), parliamentdotuk = parliamentdotuk)
