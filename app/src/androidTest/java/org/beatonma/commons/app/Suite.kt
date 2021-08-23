package org.beatonma.commons.app

import org.beatonma.commons.app.ui.screens.frontpage.MembersLayoutTest
import org.beatonma.commons.app.ui.screens.search.SearchUiTest
import org.beatonma.commons.app.ui.screens.signin.UserAccountTestSuite
import org.beatonma.commons.app.ui.screens.social.SocialTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MembersLayoutTest::class,
    SearchUiTest::class,
    SocialTestSuite::class,
    UserAccountTestSuite::class,
)
class CommonsAppTestSuite
