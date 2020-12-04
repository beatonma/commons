package org.beatonma.commons.kotlin.extensions

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import org.beatonma.commons.app.division.DivisionDetailFragment.Companion.HOUSE
import org.beatonma.commons.core.House
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.division.Division
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

fun <T : Parliamentdotuk> Fragment.navigateTo(@IdRes navigationId: Int, target: T) =
    requireView().navigateTo(navigationId, target.bundle())

fun Fragment.navigateTo(uri: Uri) =
    requireView().navigateTo(uri)

fun Division.divisionBundle() = bundleOf(
    HOUSE to house,
    PARLIAMENTDOTUK to parliamentdotuk,
)

fun Bundle?.getDivision(): BundledDivision {
    this
        ?: throw NoSuchElementException("Unable to unpack Division from bundle: House and ParliamentID arguments are required!")
    return BundledDivision(this)
}

fun parliamentIdBundle(parliamentdotuk: ParliamentID?) =
    bundleOf(PARLIAMENTDOTUK to parliamentdotuk)

fun Parliamentdotuk.bundle() = when (this) {
    is Division -> divisionBundle()
    else -> parliamentIdBundle(this.parliamentdotuk)
}

fun Vote.memberBundle() = parliamentIdBundle(this.memberId)
fun BillSponsor.memberBundle() = parliamentIdBundle(this.parliamentdotuk)

fun Bundle?.getParliamentID(): ParliamentID {
    require(this != null)
    return this.getInt(PARLIAMENTDOTUK)
}

class BundledDivision(val house: House, val parliamentdotuk: ParliamentID) {
    constructor(bundle: Bundle) : this(
        house = bundle.getSerializable(HOUSE) as House,
        parliamentdotuk = bundle.getParliamentID()
    )
}
