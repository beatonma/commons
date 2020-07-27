package org.beatonma.commons.data.deserialization.api.expected

import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.data.core.room.entities.bill.*
import org.beatonma.commons.data.core.room.entities.member.Party

/** https://snommoc.org/api/bill/393258/ */
internal fun expectedApiBill(): ApiBill = ApiBill(
    parliamentdotuk = 393258,
    title = "Deep Sea Mining",
    description = "A Bill to make provision about deep sea mining; and for connected purposes.",
    actName = "Deep Sea Mining Act",
    label = "Deep Sea Mining",
    homepage = "http://services.parliament.uk/bills/deepseamining.html",
    date = "2014-05-15".asDate(),
    ballotNumber = 4,
    billChapter = "15",
    isPrivate = false,
    isMoneyBill = false,
    publicInvolvementAllowed = false,
    publications = listOf(
        ApiBillPublication(
            parliamentdotuk = 412680,
            title = "Deep Sea Mining Act 2014 c.15"
        ),
        ApiBillPublication(
            parliamentdotuk = 411743,
            title = "HL Bill 81-EN 2013-14, Explanatory notes to the Bill"
        ),
        ApiBillPublication(
            parliamentdotuk = 411742,
            title = "HL Bill 81 2013-14, as brought from the Commons"
        ),
        ApiBillPublication(
            parliamentdotuk = 411737,
            title = "Report Stage Proceedings as at 24 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411732,
            title = "Consideration on Report"
        ),
        ApiBillPublication(
            parliamentdotuk = 411723,
            title = "Consideration of Bill as at 24 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411699,
            title = "Notices of Amendments given on 22 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411619,
            title = "Version of the bill showing changes made in committee"
        ),
        ApiBillPublication(
            parliamentdotuk = 411615,
            title = "Bill 156 2013-14 [as amended in Public Bill Committee]"
        ),
        ApiBillPublication(
            parliamentdotuk = 411603,
            title = "Public Bill Committee Proceedings as at 15 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411583,
            title = "Public Bill Committee Amendments as at 15 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411577,
            title = "Consideration in Committee:Selection 1"
        ),
        ApiBillPublication(
            parliamentdotuk = 411571,
            title = "Notices of Amendments given up to and including 13 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411521,
            title = "Notices of Amendments given on 8 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 411520,
            title = "Provisional Notices of Amendments and Added Names as at 23 January 2014"
        ),
        ApiBillPublication(
            parliamentdotuk = 410449,
            title = "Impact Assessment"
        ),
        ApiBillPublication(
            parliamentdotuk = 410415,
            title = "Briefing Paper on Second Reading"
        ),
        ApiBillPublication(
            parliamentdotuk = 410377,
            title = "Bill 14 EN 2013-14, explanatory notes to the Bill"
        ),
        ApiBillPublication(
            parliamentdotuk = 410376,
            title = "Bill 14 2013-14, as introduced"
        ),
    ),
    session = ParliamentarySession(
        parliamentdotuk = 377316,
        name = "2013-2014"
    ),
    sponsors = listOf(
        ApiBillSponsor(
            parliamentdotuk = null,
            name = "Sheryll Murray",
            party = null,
        ),
        ApiBillSponsor(
            parliamentdotuk = 1727,
            name = "Baroness Wilcox",
            party = Party(
                parliamentdotuk = 4,
                name = "Conservative"
            )
        )
    ),
    stages = listOf(
        ApiBillStage(
            parliamentdotuk = 6825,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8684,
                    date = "2014-05-14".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "Royal Assent"
        ),
        ApiBillStage(
            parliamentdotuk = 6697,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8532,
                    date = "2014-03-18".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "3rd reading"
        ),
        ApiBillStage(
            parliamentdotuk = 6645,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8462,
                    date = "2014-02-25".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "Order of Commitment discharged"
        ),
        ApiBillStage(
            parliamentdotuk = 6608,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8421,
                    date = "2014-02-07".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "2nd reading"
        ),
        ApiBillStage(
            parliamentdotuk = 6583,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8392,
                    date = "2014-01-24".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "1st reading"
        ),
        ApiBillStage(
            parliamentdotuk = 6581,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8389,
                    date = "2014-01-24".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "3rd reading"
        ),
        ApiBillStage(
            parliamentdotuk = 6547,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8341,
                    date = "2014-01-24".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "Report stage"
        ),
        ApiBillStage(
            parliamentdotuk = 6338,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 8059,
                    date = "2013-10-15".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "Ways and Means resolution"
        ),
        ApiBillStage(
            parliamentdotuk = 6294,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 7996,
                    date = "2014-01-15".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "Committee stage"
        ),
        ApiBillStage(
            parliamentdotuk = 6032,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 7688,
                    date = "2013-09-06".asDate(),
                    isFormal = false,
                    isProvisional = false
                )
            ),
            type = "2nd reading"
        ),
        ApiBillStage(
            parliamentdotuk = 6031,
            sittings = listOf(
                ApiBillStageSitting(
                    parliamentdotuk = 7687,
                    date = "2013-06-19".asDate(),
                    isFormal = true,
                    isProvisional = false
                )
            ),
            type = "1st reading"
        )
    ),
    type = BillType(
        name = "Ballot",
        description = "Private Members' Bill (Ballot Bill)"
    )
)
