package org.beatonma.commons.data.core.repository

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillRepositoryTest::class,
    MemberRepositoryTest::class,
)
class CommonsRepositoryTestSuite
