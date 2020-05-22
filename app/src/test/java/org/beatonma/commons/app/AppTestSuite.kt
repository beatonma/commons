package org.beatonma.commons.app

import org.beatonma.commons.app.memberprofile.ProfileHistoryCompressionTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ProfileHistoryCompressionTestSuite::class,
)
class AppTestSuite
