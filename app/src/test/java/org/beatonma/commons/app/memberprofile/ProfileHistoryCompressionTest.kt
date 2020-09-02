package org.beatonma.commons.app.memberprofile

import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.test.asDate
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    HistoryPartyCompressionTest::class,
    HistoryConstituencyCompressionTest::class,
)
class ProfileHistoryCompressionTestSuite



class HistoryPartyCompressionTest {
    private val labour = Party(parliamentdotuk = 15, name = "Labour")
    private val sdp = Party(parliamentdotuk = 32, name = "Social Democratic Party")
    private val libdem = Party(parliamentdotuk = 17, name = "Liberal Democrat")

    private fun association(party: Party, start: String, end: String?) =
        PartyAssociationWithParty(
            PartyAssociation(
                memberId = 1,
                partyId = party.parliamentdotuk,
                start = start.asDate(),
                end = end?.asDate()
            ),
            party
        )

    @Test
    fun compressParties_should_compress_multiple_short_associatations_into_one() {
        val parties: List<PartyAssociationWithParty> = listOf(
            association(
                labour,
                start = "2019-12-12",
                end = null
            ),
            association(
                labour,
                start = "2015-05-07",
                end = "2017-05-03",
            ),
            association(labour,
                start = "2017-06-08",
                end = "2019-11-06",
            ),
        )

        runBlocking {
            val compressed = compressParties(parties)!!

            compressed.size shouldbe 1
            val first = compressed.first()
            first.run {
                partyAssocation.memberId shouldbe 1
                party shouldbe labour
                start shouldbe "2015-05-07".asDate()
                end shouldbe null
            }
        }
    }

    @Test
    fun compressParties_should_not_compress_associations_with_different_parties() {
        val parties: List<PartyAssociationWithParty> = listOf(
            association(labour,
                start = "1974-02-28",
                end = "1981-03-02"
            ),
            association(sdp,
                start = "1981-03-02",
                end = "2013-09-05",
            ),
            association(libdem,
                start = "1988-03-03",
                end = null,
            ),
        )

        runBlocking {
            val compressed = compressParties(parties)!!

            compressed.size shouldbe 3
        }
    }


    @Test
    fun compressParties_should_compress_only_when_same_party() {
        val parties: List<PartyAssociationWithParty> = listOf(
            association(
                labour,
                start = "1974-02-28",
                end = "1981-03-02"
            ),
            association(
                labour,
                start = "1981-03-02",
                end = "2013-09-05",
            ),
            association(
                libdem,
                start = "1988-03-03",
                end = null,
            ),
        )

        runBlocking {
            val compressed = compressParties(parties)!!

            compressed.size shouldbe 2
            compressed.first().run {
                party shouldbe labour
                start shouldbe "1974-02-28".asDate()
                end shouldbe "2013-09-05".asDate()
            }
            compressed.last().run {
                party shouldbe libdem
                start shouldbe "1988-03-03".asDate()
                end shouldbe null
            }
        }
    }

    @Test
    fun compressParties_with_large_gaps_should_not_compress() {
        val parties: List<PartyAssociationWithParty> = listOf(
            association(
                labour,
                start = "1974-02-28",
                end = "1981-03-02"
            ),
            // One year gap!
            association(
                labour,
                start = "1982-03-02",
                end = "2013-09-05",
            ),
        )

        runBlocking {
            val compressed = compressParties(parties)!!
            compressed.size shouldbe 2
        }
    }
}


class HistoryConstituencyCompressionTest {
    private val thornaby = Constituency(parliamentdotuk = 146380, name = "Thornaby")
    private val stocktonSouth = Constituency(parliamentdotuk = 146260, name = "Stockton South")

    private val election1983 = Election(parliamentdotuk = 12, name = "1983 General Election", date = "1983-06-09".asDate(), electionType = "General Election")
    private val election1979 = Election(parliamentdotuk = 11, name = "1979 General Election", date = "1979-05-03".asDate(), electionType = "General Election")
    private val election1974Oct = Election(parliamentdotuk = 10, name = "1974 (Oct)  General Election", date = "1974-10-10".asDate(), electionType = "General Election")
    private val election1974Feb = Election(parliamentdotuk = 9, name = "1979 (Feb) General Election", date = "1974-02-28".asDate(), electionType = "General Election")

    private fun association(
        constituency: Constituency,
        election: Election,
        start: String,
        end: String?
    ) =
        HistoricalConstituencyWithElection(
            HistoricalConstituency(
                memberId = 1,
                electionId = election.parliamentdotuk,
                constituencyId = election.parliamentdotuk,
                start = start.asDate(),
                end = end?.asDate()
            ),
            constituency = constituency,
            election = election,
        )

    @Test
    fun compressConstituencies_should_compress_only_when_same_constituency() {
        val constituencies = listOf(
            association(stocktonSouth, election1983, start = "1983-06-09", end = "1987-06-11"),
            association(thornaby, election1979, start = "1979-05-03", end = "1983-06-09"),
            association(thornaby, election1974Oct, start = "1974-10-10", end = "1979-05-03"),
            association(thornaby, election1974Feb, start = "1974-02-28", end = "1974-10-10")
        )

        runBlocking {
            val compressed = compressConstituencies(constituencies)!!

            compressed.size shouldbe 2
            compressed.first().run {
                constituency shouldbe thornaby
                election shouldbe election1974Feb
                start shouldbe "1974-02-28".asDate()
                end shouldbe "1983-06-09".asDate()
            }
            compressed.last().run {
                constituency shouldbe stocktonSouth
            }
        }
    }
}
