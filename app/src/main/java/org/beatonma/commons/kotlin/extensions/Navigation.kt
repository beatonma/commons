package org.beatonma.commons.kotlin.extensions

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import org.beatonma.commons.app.division.DivisionDetailFragment.Companion.HOUSE
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivisionWithDivision
import org.beatonma.commons.data.core.room.entities.division.Vote

fun View.navigateTo(
    @IdRes navigationAction: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    extras: FragmentNavigator.Extras? = null,
) {
    Navigation.findNavController(this)
        .navigate(
            navigationAction,
            args,
            navOptions,
            extras)
}

fun View.navigateTo(url: String) {
    Navigation.findNavController(this)
        .navigate(url.toUri())
}

fun View.navigateTo(url: Uri) {
    Navigation.findNavController(this)
        .navigate(url)
}


fun FeaturedDivisionWithDivision.bundle() = bundleOf(
    HOUSE to division.house,
    PARLIAMENTDOTUK to featured.divisionId
)
fun Parliamentdotuk.bundle() = bundleOf(PARLIAMENTDOTUK to this.parliamentdotuk)
fun Vote.memberBundle() = bundleOf(PARLIAMENTDOTUK to this.memberId)

fun Bundle?.getParliamentID(): ParliamentID = this?.getInt(PARLIAMENTDOTUK)
    ?: throw Exception("ParliamentID not found in bundle: $this")
