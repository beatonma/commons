package org.beatonma.commons.app

import org.beatonma.commons.app.ui.components.TimelineTest
import org.beatonma.commons.app.ui.screens.memberprofile.ProfileHistoryCompressionTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ProfileHistoryCompressionTestSuite::class,
    TimelineTest::class,
)
class AppTestSuite
