package org.beatonma.commons.app

import org.beatonma.commons.app.search.SearchUiTest
import org.beatonma.commons.app.signin.SigninTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    SearchUiTest::class,
    SigninTestSuite::class,
)
class CommonsAppTestSuite
