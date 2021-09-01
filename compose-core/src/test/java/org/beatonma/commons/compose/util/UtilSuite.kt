package org.beatonma.commons.compose.util

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ColorTest::class,
    StringWithAnnotatedStyleTest::class,
    StringWithAnnotatedUrlsTest::class,
)
class UtilSuite
