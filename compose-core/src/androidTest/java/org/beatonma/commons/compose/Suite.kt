package org.beatonma.commons.compose

import org.beatonma.commons.compose.components.ComponentTestSuite
import org.beatonma.commons.compose.modifiers.ModifierTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ComponentTestSuite::class,
    ModifierTestSuite::class,
)
class ComposeTestSuite
