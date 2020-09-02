package org.beatonma.commons.app.bill

import kotlinx.coroutines.runBlocking
import org.beatonma.commons.anyBillStage
import org.beatonma.commons.anyBillStageSitting
import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings
import org.beatonma.commons.test.asDate
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

private val stages = listOf(
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 244564, stageType = "1st reading"),
        sittings = listOf(
            anyBillStageSitting(244564, date = "2012-08-04".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 392162, stageType = "2nd reading"),
        sittings = listOf(
            anyBillStageSitting(392162, date = "2012-08-12".asDate()),
            anyBillStageSitting(392162, date = "2012-08-15".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 168234, stageType = "Committee stage"),
        sittings = listOf(
            anyBillStageSitting(168234, date = "2012-08-18".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 384281, stageType = "Report stage"),
        sittings = listOf(
            anyBillStageSitting(384281, date = "2012-09-11".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 297273, stageType = "3rd reading"),
        sittings = listOf(
            anyBillStageSitting(297273, date = "2012-09-13".asDate()),
        )
    ),


    // 2nd House
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 642813, stageType = "1st reading"),
        sittings = listOf(
            anyBillStageSitting(642813, date = "2012-09-21".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 684251, stageType = "2nd reading"),
        sittings = listOf(
            anyBillStageSitting(684251, date = "2012-09-24".asDate()),
            anyBillStageSitting(684251, date = "2012-09-27".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 482162, stageType = "Committee stage"),
        sittings = listOf(
            anyBillStageSitting(482162, date = "2012-11-17".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 381272, stageType = "Report stage"),
        sittings = listOf(
            anyBillStageSitting(381272, date = "2013-01-23".asDate()),
        )
    ),
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 12625, stageType = "3rd reading"),
        sittings = listOf(
            anyBillStageSitting(12625, date = "2013-01-29".asDate()),
        )
    ),


    // 'Ping-pong' between houses: https://www.parliament.uk/about/how/laws/passage-bill/commons/coms-consideration-of-amendments/
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 272192, stageType = "Consideration of Commons Amendments"),
        sittings = listOf(
            anyBillStageSitting(272192, date = "2013-02-19".asDate()),
        )
    ),

    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 548261, stageType = "Consideration of Lords Amendments"),
        sittings = listOf(
            anyBillStageSitting(548261, date = "2013-02-26".asDate()),
        )
    ),

    // Bill becomes law
    BillStageWithSittings(
        stage = anyBillStage(parliamentdotuk = 267121, stageType = "Royal Assent"),
        sittings = listOf(
            anyBillStageSitting(267121, date = "2013-03-13".asDate()),
        )
    ),
)

class BillDetailViewModelTest {
    @Test
    fun `classifyProgress is correct`() {
        classifyProgress("1st reading", previous = null) shouldbe
                BillStageProgress.FirstFirstReading  // previous is not set so first matching value should be taken

        classifyProgress("1st reading", previous = BillStageProgress.FirstFirstReading) shouldbe
                BillStageProgress.FirstFirstReading  // Same as previous - we haven't seen any later stage yet so don't assume they exist

        classifyProgress("1st reading", previous = BillStageProgress.FirstSecondReading) shouldbe
                BillStageProgress.SecondFirstReading  // We have seen the first "2nd reading" so this "1st reading" is from the second House

        classifyProgress("1st reading", previous = BillStageProgress.SecondSecondReading) shouldbe
                BillStageProgress.SecondSecondReading  // Same as previous - there is no "1st reading" after the "2nd reading" in the second house - something is weird, return previous value

        classifyProgress("3rd reading", previous = BillStageProgress.FirstSecondReading) shouldbe
                BillStageProgress.FirstThirdReading

        classifyProgress("3rd reading", previous = BillStageProgress.SecondSecondReading) shouldbe
                BillStageProgress.SecondThirdReading

        classifyProgress("3rd reading", previous = BillStageProgress.SecondSecondReading) shouldbe
                BillStageProgress.SecondThirdReading

        classifyProgress("Consideration of Commons amendments", previous = BillStageProgress.SecondThirdReading) shouldbe
                BillStageProgress.ConsiderationOfAmendments  // Unique value, value of previous does not matter

        classifyProgress("Royal Assent", previous = BillStageProgress.SecondSecondReading) shouldbe
                BillStageProgress.RoyalAssent  // Unique value, value of previous does not matter


        classifyProgress("Carry-over motion", previous = BillStageProgress.FirstThirdReading) shouldbe
                BillStageProgress.FirstThirdReading  // Unhandled stage type - return previous value

        classifyProgress("Carry-over motion", previous = null) shouldbe
                null  // Unhandled stage type - return previous value
    }

    @Test
    fun `getAnnotatedStages is correct`() {
        runBlocking {
            val annotated = getAnnotatedStages(
                originatingHouse = House.commons,
                stagesWithSittings = stages
            )

            annotated.size shouldbe 13

            // 1st House
            annotated[0].run {
                house shouldbe House.commons
                progress shouldbe BillStageProgress.FirstFirstReading
            }
            annotated[1].run {
                house shouldbe House.commons
                progress shouldbe BillStageProgress.FirstSecondReading
            }
            annotated[2].run {
                house shouldbe House.commons
                progress shouldbe BillStageProgress.FirstCommitteeStage
            }
            annotated[3].run {
                house shouldbe House.commons
                progress shouldbe BillStageProgress.FirstReportStage
            }
            annotated[4].run {
                house shouldbe House.commons
                progress shouldbe BillStageProgress.FirstThirdReading
            }

            // 2nd House
            annotated[5].run {
                house shouldbe House.lords
                progress shouldbe BillStageProgress.SecondFirstReading
            }
            annotated[6].run {
                house shouldbe House.lords
                progress shouldbe BillStageProgress.SecondSecondReading
            }
            annotated[7].run {
                house shouldbe House.lords
                progress shouldbe BillStageProgress.SecondCommitteeStage
            }
            annotated[8].run {
                house shouldbe House.lords
                progress shouldbe BillStageProgress.SecondReportStage
            }
            annotated[9].run {
                house shouldbe House.lords
                progress shouldbe BillStageProgress.SecondThirdReading
            }

            // Consideration of amendments
            annotated[10].run {
                house shouldbe null
                progress shouldbe BillStageProgress.ConsiderationOfAmendments
            }
            annotated[11].run {
                house shouldbe null
                progress shouldbe BillStageProgress.ConsiderationOfAmendments
            }

            // Royal Assent
            annotated[12].run {
                house shouldbe null
                progress shouldbe BillStageProgress.RoyalAssent
            }
        }
    }
}
