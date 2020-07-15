package org.beatonma.commons.app.bill

import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Test

class BillDetailViewModelTest {
    @Test
    fun classifyProgress_is_corrent() {
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
    }
}
