package org.beatonma.commons.app

import org.beatonma.commons.app.memberprofile.MemberProfileViewModelTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MemberProfileViewModelTestSuite::class,
)
class AppTestSuite
