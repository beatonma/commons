package org.beatonma.commons.app

import org.beatonma.commons.app.search.SearchUiTest
import org.beatonma.commons.app.signin.UserAccountTestSuite
import org.beatonma.commons.app.social.SocialTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    SearchUiTest::class,
    SocialTestSuite::class,
    UserAccountTestSuite::class,
)
class CommonsAppTestSuite
