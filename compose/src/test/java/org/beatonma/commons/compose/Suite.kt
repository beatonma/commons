package org.beatonma.commons.compose

import org.beatonma.commons.compose.components.ComponentsSuite
import org.beatonma.commons.compose.util.UtilSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ComponentsSuite::class,
    UtilSuite::class,
)
class Suite
