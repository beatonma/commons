package org.beatonma.commons.compose.modifiers

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    WrapContentHeightTest::class,
    WrapContentWidthTest::class,
    WrapContentSizeTest::class,
    WrapContentOrFillWidthTest::class,
    WrapContentOrFillHeightTest::class,
    WrapContentOrFillSizeTest::class,
)
class ModifierTestSuite
