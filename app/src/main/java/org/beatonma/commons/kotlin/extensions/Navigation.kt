package org.beatonma.commons.kotlin.extensions

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.beatonma.commons.app.division.DivisionDetailFragment.Companion.HOUSE
import org.beatonma.commons.core.House
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.models.search.SearchResult

fun Fragment.navigateTo(searchResult: SearchResult) = navigateTo(searchResult.toUri())

fun Fragment.navigateToMember(memberID: ParliamentID) =
    navigateTo(CommonsService.getMemberUrl(memberID).toUri())

fun Fragment.navigateTo(memberProfile: MemberProfile) =
    navigateToMember(memberProfile.parliamentdotuk)

fun Fragment.navigateTo(constituency: Constituency) =
    navigateTo(CommonsService.getConstituencyUrl(constituency.parliamentdotuk).toUri())

fun Fragment.navigateTo(division: Division) =
    navigateTo(CommonsService.getDivisionUrl(division.house, division.parliamentdotuk).toUri())

fun Fragment.navigateTo(bill: Bill) =
    navigateTo(CommonsService.getBillUrl(bill.parliamentdotuk).toUri())


val Bundle?.parliamentID: ParliamentID
    get() {
        require(this != null)
        return getInt(PARLIAMENTDOTUK)
    }

fun Bundle?.getDivision(): BundledDivision {
    require(this != null)
    return BundledDivision(this)
}

data class BundledDivision(val house: House, val parliamentdotuk: ParliamentID) {
    constructor(bundle: Bundle) : this(
        house = bundle.getSerializable(HOUSE) as House,
        parliamentdotuk = bundle.parliamentID
    )
}

private fun Fragment.navigateTo(uri: Uri) = requireView().navigateTo(uri)

private fun View.navigateTo(url: Uri) {
    Navigation.findNavController(this)
        .navigate(url)
}
