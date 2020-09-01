package org.beatonma.commons.snommoc.models.social

import android.os.Bundle
import androidx.core.os.bundleOf
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID


data class SocialTarget(val targetType: SocialTargetType, val parliamentdotuk: ParliamentID) {
    constructor(bundle: Bundle): this(
        bundle.get("target_type") as SocialTargetType,
        bundle.getInt(PARLIAMENTDOTUK),
    )

    fun toBundle() = bundleOf(
        "target_type" to targetType,
        PARLIAMENTDOTUK to parliamentdotuk
    )
}
