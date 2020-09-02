package org.beatonma.commons.kotlin

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    GraphicsUnitTest::class,
    FloatTest::class,
    IntTest::class
)
class KotlinUtilTestSuite
