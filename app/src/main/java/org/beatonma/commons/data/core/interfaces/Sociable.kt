package org.beatonma.commons.data.core.interfaces

import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialTargetType

interface Sociable: Parliamentdotuk {
    fun getSocialContentTarget(): SocialTargetType
    fun asSocialTarget() = SocialTarget(this)
}

interface Commentable: Sociable
interface Votable: Sociable
