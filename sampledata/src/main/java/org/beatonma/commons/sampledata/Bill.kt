package org.beatonma.commons.sampledata

import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillData
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.Organisation
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.snommoc.models.ApiBill
import org.beatonma.commons.snommoc.models.ApiBillPublication
import org.beatonma.commons.snommoc.models.ApiBillPublicationLink
import org.beatonma.commons.snommoc.models.ApiBillSponsor
import org.beatonma.commons.snommoc.models.ApiBillStage
import org.beatonma.commons.snommoc.models.ApiBillType
import org.beatonma.commons.snommoc.models.ApiOrganisation
import org.beatonma.commons.snommoc.models.ApiSession
import java.time.LocalDate
import java.time.LocalDateTime


val SampleOrganisation = Organisation(
    name = "HM Treasury",
    url = "https://www.gov.uk/government/organisations/hm-treasury"
)

val SampleBill = Bill(
    data = BillData(
        id = 2835,
        title = "Finance Act 2021",
        lastUpdate = LocalDateTime.parse("2021-06-15T00:09:41.254035"),
        description = null,
        isAct = true,
        isDefeated = false,
        withdrawnAt = null,
        billTypeId = 1,
        sessionIntroducedId = 35,
        currentStageId = 15321
    ),
    type = BillType(
        id = 1,
        name = "Government Bill",
        description = """Government Bills are a type of Public Bill, introduced by government ministers . A Government Bill is a formal proposal for a new law, or a change in the law, that is put forward by the Government for consideration by Parliament.The Queen 's Speech normally lists the Bills that the Government are intending to put forward during the parliamentary year. Government Bills are normally Public Bills.<div><a href='https ://erskinemay.parliament.uk/section/4972/government-and-private-members-bills/'>Find out more about Government Bills</a></div><div><a href='https://www.parliament.uk/about/how/laws/bills/public/'>Find out more about Public Bills</a></div>""",
        category = "Public"
    ),
    currentStage = BillStage(
        parliamentdotuk = 15321,
        description = "Royal Assent",
        house = House.unassigned,
        sessionId = 36,
        sittings = listOf(LocalDate.parse("2021-06-10")),
        latestSitting = LocalDate.parse("2021-06-10"),
    ),
    stages = listOf(
        BillStage(
            parliamentdotuk = 12752,
            description = "1st reading",
            house = House.commons,
            sessionId = 35,
            sittings = listOf(LocalDate.parse("2021-03-09")),
            latestSitting = LocalDate.parse("2021-03-09"),
        ),
        BillStage(
            parliamentdotuk = 12827,
            description = "2nd reading",
            house = House.commons,
            sessionId = 35,
            sittings = listOf(LocalDate.parse("2021-04-13")),
            latestSitting = LocalDate.parse("2021-04-13"),
        ),
        BillStage(
            parliamentdotuk = 15321,
            description = "Royal Assent",
            house = House.unassigned,
            sessionId = 36,
            sittings = listOf(LocalDate.parse("2021-06-10")),
            latestSitting = LocalDate.parse("2021-06-10"),
        )
    ),
    sessionIntroduced = ParliamentarySession(id = 35, name = "2019 - 2021"),
    sessions = listOf(
        ParliamentarySession(id = 36, name = "2021 - 2022"),
        ParliamentarySession(id = 35, name = "2019 - 2021")
    ),
    publications = listOf(
        BillPublication(
            data = BillPublicationData(parliamentdotuk = 40653,
                title = "Budget Resolutions",
                date = LocalDate.parse("2021-03-03"),
                type = "Relevant documents"),
            links = listOf(
                BillPublicationLink(
                    publicationId = 40653,
                    title = "Budget Resolutions",
                    url = "https://publications.parliament.uk/pa/bills/cbill/58-01/FinanceDocuments/BudgetResos0321.pdf",
                    contentType = "application/pdf")
            )
        ),
        BillPublication(
            data = BillPublicationData(
                parliamentdotuk = 40654,
                title = "Explanatory Notes to the Budget Resolutions",
                date = LocalDate.parse("2021-03-03"),
                type = "Relevant documents"
            ),
            links = listOf(
                BillPublicationLink(
                    publicationId = 40654,
                    title = "Explanatory Notes to the Budget Resolutions",
                    url = "https://publications.parliament.uk/pa/bills/cbill/58-01/FinanceDocuments/BudgetResosNotes0321.pdf",
                    contentType = "application/pdf")
            )
        ),
    ),
    sponsors = listOf(
        BillSponsor(
            1,
            member = SampleMember,
            organisation = SampleOrganisation,
        )
    ),
)

val SampleApiSession = ApiSession(
    parliamentdotuk = 35,
    name = "2019-2021"
)

val SampleApiBillType = ApiBillType(
    parliamentdotuk = 1,
    name = "Government Bill",
    description = """Government Bills are a type of Public Bill, introduced by government ministers . A Government Bill is a formal proposal for a new law, or a change in the law, that is put forward by the Government for consideration by Parliament.The Queen 's Speech normally lists the Bills that the Government are intending to put forward during the parliamentary year. Government Bills are normally Public Bills.<div><a href='https ://erskinemay.parliament.uk/section/4972/government-and-private-members-bills/'>Find out more about Government Bills</a></div><div><a href='https://www.parliament.uk/about/how/laws/bills/public/'>Find out more about Public Bills</a></div>""",
    category = "Public"
)

val SampleApiOrganisation = ApiOrganisation(
    name = "HM Treasury",
    url = "https://www.gov.uk/government/organisations/hm-treasury",
)

val SampleApiBillSponsor = ApiBillSponsor(
    id = 5,
    member = SampleApiProfile,
    organisation = SampleApiOrganisation,
)

val SampleApiBillStage = ApiBillStage(
    parliamentdotuk = 15321,
    description = "Royal Assent",
    house = House.unassigned,
    session = SampleApiSession,
    sittings = listOf(LocalDate.parse("2021-06-10")),
    latestSitting = LocalDate.parse("2021-06-10"),
)

val SampleApiBillPublicationLink = ApiBillPublicationLink(
    title = "Finance Act 2021 (c. 26)",
    url = "https =//www.legislation.gov.uk/ukpga/2021/26/enacted",
    contentType = "text/html"
)

val SampleApiBillPublication = ApiBillPublication(
    parliamentdotuk = 41847,
    title = "Finance Act 2021 (c. 26)",
    date = LocalDate.parse("2021-06-10"),
    links = listOf(
        SampleApiBillPublicationLink,
    ),
    type = "Act of Parliament",
)

val SampleApiBill = ApiBill(
    parliamentdotuk = 2835,
    title = "Finance Act 2021",
    lastUpdate = LocalDateTime.parse("2021-06-15T00:09:41.254035"),
    description = null,
    isAct = true,
    isDefeated = false,
    withdrawnAt = null,
    type = SampleApiBillType,
    sessionIntroduced = SampleApiSession,
    currentStage = SampleApiBillStage,
    publications = listOf(
        SampleApiBillPublication
    ),
    sessions = listOf(
        SampleApiSession,
    ),
    sponsors = listOf(
        SampleApiBillSponsor,
    ),
    stages = listOf(
        SampleApiBillStage,
    )
)
