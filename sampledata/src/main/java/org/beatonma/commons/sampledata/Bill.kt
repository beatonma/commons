package org.beatonma.commons.sampledata

import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillStageSitting
import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.member.Party
import java.time.LocalDate

val SampleBillSponsor: BillSponsor get() = SampleBillSponsorWithParty.sponsor
val SampleBillSponsorWithParty: BillSponsorWithParty get() = SampleBillSponsors.random()
val SampleBillPublication get() = SampleBillPublications.random()
val SampleSession get() = ParliamentarySession(parliamentdotuk = 377316, name = "2013 - 2014")
val SampleBillType
    get() = BillType(
        name = "Ballot",
        description = "Private Members' Bill (Ballot Bill))",
    )

val SampleBill: Bill
    get() = Bill(
        parliamentdotuk = 393258,
        title = "Deep Sea Mining",
        description = "A Bill to make provision about deep sea mining; and for connected purposes.",
        actName = "Deep Sea Mining Act",
        label = "Deep Sea Mining",
        homepage = "http://services.parliament.uk/bills/deepseamining.html",
        date = LocalDate.of(2014, 5, 15),
        ballotNumber = 4,
        billChapter = "15",
        isPrivate = false,
        isMoneyBill = false,
        publicInvolvementAllowed = false,
        sessionId = 377316,
        typeId = "Ballot"
    )

val SampleCompleteBill: CompleteBill
    get() = CompleteBill(
        bill = SampleBill,
        session = SampleSession,
        type = SampleBillType,
        publications = SampleBillPublications,
        sponsors = SampleBillSponsors,
        stages = SampleBillStages,
    )

val SampleBillSponsors
    get() = listOf(
        BillSponsorWithParty(
            sponsor = BillSponsor(
                name = "Sheryll Murray",
                billId = 393258,
                parliamentdotuk = null,
                partyId = null
            ),
            party = null
        ),
        BillSponsorWithParty(
            sponsor = BillSponsor(
                name = "Baroness Wilcox",
                billId = 393258,
                parliamentdotuk = 1727,
                partyId = 4
            ),
            party = Party(
                parliamentdotuk = 4,
                name = "Conservative"
            )
        )
    )

val SampleBillPublications
    get() = listOf(
        BillPublication(
            parliamentdotuk = 410376,
            billId = 393258,
            title = "Bill 14 2013 - 14, as introduced",
            url = null, contentType = null, contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 410377,
            billId = 393258,
            title = "Bill 14 EN 2013 - 14, explanatory notes to the Bill",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 410415,
            billId = 393258,
            title = "Briefing Paper on Second Reading",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 410449,
            billId = 393258,
            title = "Impact Assessment",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 411520,
            billId = 393258,
            title = "Provisional Notices of Amendments and Added Names as at23 January 2014",
            url = "http://www.publications.parliament.uk/pa/bills/cbill/2013-2014/0014/amend/deepseaaddednames.pdf",
            contentType = "application/pdf",
            contentLength = 119946
        ),
        BillPublication(
            parliamentdotuk = 411521,
            billId = 393258,
            title = "Notices of Amendments given on 8 January 2014",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 411571,
            billId = 393258,
            title = "Notices of Amendments given up to and including 13 January 2014",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 411577,
            billId = 393258,
            title = "Consideration in Committee:Selection 1",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 411583,
            billId = 393258,
            title = "Public Bill Committee Amendments as at 15 January 2014",
            url = null,
            contentType = null,
            contentLength = null
        ),
        BillPublication(
            parliamentdotuk = 411603,
            billId = 393258,
            title = "Public Bill Committee Proceedings as at 15 January 2014",
            url = "http://www.publications.parliament.uk/pa/bills/cbill/2013-2014/0014/pro0141501p.1-6.html",
            contentType = "text/html",
            contentLength = 93174
        ),
    )

val SampleBillStages
    get() = listOf(
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6031,
                billId = 393258,
                type = "1st reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 7687,
                    billStageId = 6031,
                    date = LocalDate.of(2013, 6, 19),
                    isFormal = true,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6032,
                billId = 393258,
                type = "2nd reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 7688,
                    billStageId = 6032,
                    date = LocalDate.of(2013, 9, 6),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6294,
                billId = 393258,
                type = "Committee stage"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 7996,
                    billStageId = 6294,
                    date = LocalDate.of(2014, 1, 15),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6338,
                billId = 393258,
                type = "Ways and Means resolution"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8059,
                    billStageId = 6338,
                    date = LocalDate.of(2013, 10, 15),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6547,
                billId = 393258,
                type = "Report stage"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8341,
                    billStageId = 6547,
                    date = LocalDate.of(2014, 1, 24),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6581,
                billId = 393258,
                type = "3rd reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8389,
                    billStageId = 6581,
                    date = LocalDate.of(2014, 1, 24),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6583,
                billId = 393258,
                type = "1st reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8392,
                    billStageId = 6583,
                    date = LocalDate.of(2014, 1, 24),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6608,
                billId = 393258,
                type = "2nd reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8421,
                    billStageId = 6608,
                    date = LocalDate.of(2014, 2, 7),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6645,
                billId = 393258,
                type = "Order of Commitment discharged"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8462,
                    billStageId = 6645,
                    date = LocalDate.of(2014, 2, 25),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6697,
                billId = 393258,
                type = "3rd reading"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8532,
                    billStageId = 6697,
                    date = LocalDate.of(2014, 3, 18),
                    isFormal = false,
                    isProvisional = false
                )
            )
        ),
        BillStageWithSittings(
            stage = BillStage(
                parliamentdotuk = 6825,
                billId = 393258,
                type = "Royal Assent"
            ),
            sittings = listOf(
                BillStageSitting(
                    parliamentdotuk = 8684,
                    billStageId = 6825,
                    date = LocalDate.of(2014, 5, 14),
                    isFormal = false,
                    isProvisional = false
                )
            )
        )
    )
