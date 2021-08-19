package org.beatonma.commons.core

import org.beatonma.commons.core.extensions.ExtensionsTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ExtensionsTestSuite::class,
)
class CoreModuleTestSuite
