package org.beatonma.commons.repo.repository

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillRepositoryTest::class,
    MemberRepositoryTest::class,
    ConstituencyRepositoryTest::class,
)
class RepositorySuite
