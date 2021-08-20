package org.beatonma.commons.core.extensions

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CollectionsTest::class,
    FloatTest::class,
    IntTest::class,
    IntFlagTest::class,
)
class ExtensionsTestSuite
