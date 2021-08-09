package org.beatonma.commons.app.social

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CommentListTest::class,
    CreateCommentUiTest::class,
    SocialIconsTest::class,
)
class SocialTestSuite
