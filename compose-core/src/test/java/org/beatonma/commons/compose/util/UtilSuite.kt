package org.beatonma.commons.compose.util

import org.beatonma.commons.compose.util.math.Matrix2x2Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ColorTest::class,
    Matrix2x2Test::class,
    StringWithAnnotatedStyleTest::class,
    StringWithAnnotatedUrlsTest::class,
)
class UtilSuite
