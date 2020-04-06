package org.beatonma.commons.data.core.room.dao.testdata

import org.beatonma.commons.data.core.room.entities.bill.*

// Mix of https://snommoc.org/api/bill/392545/ and https://snommoc.org/api/bill/393258/
// picking and choosing useful values.
const val BILL_PUK = 392545
val API_BILL = ApiBill(
    parliamentdotuk = 392545,
    title = "Presumption of Death",
    description = "A Bill to make provision in relation to the presumed deaths of missing persons; and for connected purposes.",
    actName = "Deep Sea Mining Act",
    label = "Presumption of Death",
    homepage = "http://services.parliament.uk/bills/presumptionofdeath.html",
    date = "2009-06-25",
    ballotNumber = 4,
    billChapter = "15",
    isPrivate = false,
    isMoneyBill = false,
    publicInvolvementAllowed = true,
    publications = listOf(
        BillPublication(
            billId = BILL_PUK,
            parliamentdotuk = 397898,
            title = "Bill as introduced"
        ),
    ),
    session = ParliamentarySession(
        parliamentdotuk = 377312,
        name = "2008-2009"
    ),
    sponsors = listOf(
        BillSponsor(
            billId = BILL_PUK,
            parliamentdotuk = 1727,
            name = "Baroness Wilcox"
        ),
    ),
    stages = listOf(
        ApiBillStage(
            parliamentdotuk = 6697,
            sittings = listOf(
                BillStageSitting(
                    billStageId = 6697,
                    parliamentdotuk = 8684,
                    date = "2014-05-14",
                    isFormal = false,
                    isProvisional = true,
                )
            ),
            type = "Order of Commitment discharged"
        )
    ),
    type = BillType(
        name = "Ballot",
        description = "Private Members' Bill (Ballot Bill)"
    )
)
