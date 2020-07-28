package org.beatonma.commons.data.deserialization

import org.beatonma.commons.data.deserialization.api.LiveDeserializationTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DivisionDeserializeTest::class,
    LiveDeserializationTest::class,
)
class DeserializationTestSuite
