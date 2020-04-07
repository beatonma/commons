package org.beatonma.commons

import org.beatonma.commons.data.DataTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DataTestSuite::class,
)
class CommonsTestSuite
