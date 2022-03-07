package org.beatonma.commons.repo.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.repo.BaseRoomTest
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.sampledata.SampleApiBill
import org.beatonma.commons.sampledata.SampleApiBillPublication
import org.beatonma.commons.sampledata.SampleApiBillPublicationLink
import org.beatonma.commons.sampledata.SampleApiBillSponsor
import org.beatonma.commons.sampledata.SampleApiBillStage
import org.beatonma.commons.sampledata.SampleApiBillType
import org.beatonma.commons.sampledata.SampleApiSession
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.fakeOf
import org.junit.Before
import org.junit.Test

class BillRepositoryTest : BaseRoomTest() {
    lateinit var repository: BillRepository

    private val billDao: BillDao
        get() = db.billDao()
    private val memberDao: MemberDao
        get() = db.memberDao()

    @Before
    override fun setUp() {
        super.setUp()
        repository = BillRepository(
            @Suppress("RedundantSuspendModifier", "unused", "UNUSED_PARAMETER")
            fakeOf(CommonsApi::class, object {
                suspend fun getBill(id: Int) = Success(SampleApiBill)
            }),
            billDao,
            memberDao,
        )

        runBlocking(Dispatchers.Main) {
            repository.saveApiBill(SampleApiBill)
        }
    }

    @Test
    fun bill_is_saved_and_retrieved_correctly() {
        runQuery(
            { repository.getBill(SampleApiBill.parliamentdotuk) }
        ) { bill ->
            with(bill.data) {
                val expected = SampleApiBill
                id shouldbe expected.parliamentdotuk
                title shouldbe expected.title
                lastUpdate shouldbe expected.lastUpdate
                description shouldbe expected.description
                isAct shouldbe expected.isAct
                isDefeated shouldbe expected.isDefeated
                withdrawnAt shouldbe expected.withdrawnAt
            }

            with(bill.type) {
                val expected = SampleApiBillType
                id shouldbe expected.parliamentdotuk
                name shouldbe expected.name
                description shouldbe expected.description
                category shouldbe expected.category
            }

            with(bill.currentStage) {
                val expected = SampleApiBillStage
                parliamentdotuk shouldbe expected.parliamentdotuk
                description shouldbe expected.description
                house shouldbe expected.house
                sittings shouldbe expected.sittings
                latestSitting shouldbe expected.latestSitting
            }

            with(bill.sessionIntroduced) {
                val expected = SampleApiSession
                id shouldbe expected.parliamentdotuk
                name shouldbe expected.name
            }

            with(bill.sessions.first()) {
                val expected = SampleApiSession
                id shouldbe expected.parliamentdotuk
                name shouldbe expected.name
            }

            with(bill.publications.first()) {
                val expected = SampleApiBillPublication
                data.parliamentdotuk shouldbe expected.parliamentdotuk
                data.type shouldbe expected.type
                data.title shouldbe expected.title
                data.date shouldbe expected.date

                with(links.first()) {
                    val _expected = SampleApiBillPublicationLink
                    title shouldbe _expected.title
                    contentType shouldbe _expected.contentType
                    url shouldbe _expected.url
                }
            }

            with(bill.stages.first()) {
                val expected = SampleApiBillStage
                parliamentdotuk shouldbe expected.parliamentdotuk
                description shouldbe expected.description
                house shouldbe expected.house
                sittings shouldbe expected.sittings
                latestSitting shouldbe expected.latestSitting
            }

            with(bill.sponsors.first()) {
                val expected = SampleApiBillSponsor
                id shouldbe expected.id

                member!!.name shouldbe expected.member!!.name
                organisation!!.name shouldbe expected.organisation!!.name
            }
        }
    }
}
