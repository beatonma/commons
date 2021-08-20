package org.beatonma.commons

import org.beatonma.commons.app.AppTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AppTestSuite::class,
)
class CommonsTestSuite
