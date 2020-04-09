package org.beatonma.commons.data.deserialization

import com.squareup.moshi.Moshi
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.testdata.API_DIVISION
import org.beatonma.commons.data.testdata.API_DIVISION_JSON
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test

class DivisionDeserializeTest {
    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun ensure_ApiDivision_is_parsed_from_JSON_correctly() {
        // Mostly making sure that the voteType enum value is handled correctly
        val parsed = moshi.adapter(ApiDivision::class.java).fromJson(API_DIVISION_JSON)
        println(parsed)
        parsed shouldbe API_DIVISION
    }
}
