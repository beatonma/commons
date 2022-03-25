package org.beatonma.commons.app.util

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.models.search.SearchResult

private const val CONSTITUENCY_ID = "constituency_id"
private const val ELECTION_ID = "election_id"

fun Fragment.navigateTo(searchResult: SearchResult) = navigateTo(searchResult.toUri())

fun Fragment.navigateToMember(memberID: ParliamentID) =
    navigateTo(CommonsService.getMemberUrl(memberID).toUri())

fun Fragment.navigateToConstituencyResult(constituencyId: ParliamentID, electionId: ParliamentID) =
    navigateTo(CommonsService.getConstituencyResultsUrl(constituencyId, electionId).toUri())

fun Fragment.navigateTo(memberProfile: MemberProfile) =
    navigateToMember(memberProfile.parliamentdotuk)

fun Fragment.navigateTo(constituency: Constituency) =
    navigateTo(CommonsService.getConstituencyUrl(constituency.parliamentdotuk).toUri())

fun Fragment.navigateTo(division: ZeitgeistDivision) =
    navigateTo(CommonsService.getDivisionUrl(division.house, division.id).toUri())

fun Fragment.navigateTo(bill: ZeitgeistBill) =
    navigateTo(CommonsService.getBillUrl(bill.id).toUri())


val Bundle?.parliamentID: ParliamentID
    get() {
        require(this != null)
        return getInt(PARLIAMENTDOTUK)
    }

fun Bundle?.getDivision(): BundledDivision {
    require(this != null)
    return BundledDivision(this)
}

fun Bundle?.getConstituencyResult(): BundledConstituencyResult {
    require(this != null)
    return BundledConstituencyResult(this)
}

class BundledConstituencyResult(val constituencyId: ParliamentID, val electionId: ParliamentID) {
    constructor(bundle: Bundle) : this(
        constituencyId = bundle.getInt(CONSTITUENCY_ID),
        electionId = bundle.getInt(ELECTION_ID)
    )
}

class BundledDivision(val parliamentdotuk: ParliamentID) {
    constructor(bundle: Bundle) : this(parliamentdotuk = bundle.parliamentID)
}

private fun Fragment.navigateTo(uri: Uri) =
    Navigation.findNavController(requireView())
        .navigate(uri)

@Composable
fun findNavigationController() = Navigation.findNavController(LocalView.current)

fun NavController.navigateTo(uri: String) {
    navigate(uri.toUri())
}

fun NavController.navigateTo(searchResult: SearchResult) {
    navigate(searchResult.toUri())
}

fun NavController.navigateToMember(memberID: ParliamentID) {
    navigate(CommonsService.getMemberUrl(memberID))
}

fun NavController.navigateToConstituencyResult(
    constituencyId: ParliamentID,
    electionId: ParliamentID,
) {
    navigate(CommonsService.getConstituencyResultsUrl(constituencyId, electionId))
}

fun NavController.navigateTo(memberProfile: MemberProfile) {
    navigateToMember(memberProfile.parliamentdotuk)
}

fun NavController.navigateTo(constituency: Constituency) {
    navigateTo(CommonsService.getConstituencyUrl(constituency.parliamentdotuk))
}

fun NavController.navigateTo(division: ZeitgeistDivision) {
    navigateTo(CommonsService.getDivisionUrl(division.house, division.id))
}

fun NavController.navigateTo(bill: ZeitgeistBill) {
    navigateTo(CommonsService.getBillUrl(bill.id))
}
