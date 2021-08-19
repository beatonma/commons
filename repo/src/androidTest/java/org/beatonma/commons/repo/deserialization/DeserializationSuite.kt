package org.beatonma.commons.repo.deserialization

import org.beatonma.commons.repo.deserialization.api.LiveDeserializationTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DivisionDeserializeTest::class,
    LiveDeserializationTest::class,
)
class DeserializationSuite
