package org.beatonma.commons.repo.deserialization

import com.squareup.moshi.Moshi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.beatonma.commons.repo.testdata.API_DIVISION
import org.beatonma.commons.repo.testdata.API_DIVISION_JSON
import org.beatonma.commons.snommoc.models.ApiDivision
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DivisionDeserializeTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var moshi: Moshi

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun ensure_ApiDivision_is_parsed_from_JSON_correctly() {
        // Mostly making sure that the voteType enum value is handled correctly
        val parsed = moshi.adapter(ApiDivision::class.java)
            .fromJson(API_DIVISION_JSON)
        parsed shouldbe API_DIVISION
    }
}
