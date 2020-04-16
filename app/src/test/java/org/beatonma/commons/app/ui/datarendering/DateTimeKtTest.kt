package org.beatonma.commons.app.ui.datarendering

import org.beatonma.lib.testing.kotlin.extensions.assertions.assertEquals
import org.junit.Test
import java.util.*

class DateTimeKtTest {
    @Test
    fun testFormatDate() {
        val calendar = Calendar.getInstance()
        formatDate("1982-09-13", calendar).apply {
            assertEquals("13 September 1982")
        }
    }
}
