package org.beatonma.commons.app.signin

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DeleteAccountUiTest::class,
    EditableUsernameTest::class,
    SignInUiTest::class,
    UserAccountUiTest::class,
)
class UserAccountTestSuite
