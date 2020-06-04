package org.beatonma.commons.data.core.interfaces

import org.beatonma.commons.data.core.social.SocialContentTarget

interface Sociable: Parliamentdotuk {
    fun getSocialContentTarget(): SocialContentTarget
}

interface Commentable: Sociable
interface Votable: Sociable
