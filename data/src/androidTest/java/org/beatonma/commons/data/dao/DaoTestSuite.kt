package org.beatonma.commons.data.dao

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillDaoTest::class,
    DivisionDaoTest::class,
    MemberDaoTest::class,
)
class DaoTestSuite
