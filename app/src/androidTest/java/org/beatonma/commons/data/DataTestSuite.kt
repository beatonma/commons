package org.beatonma.commons.data

import org.beatonma.commons.data.core.room.dao.BillDaoTestSuite
import org.beatonma.commons.data.core.room.dao.MemberDaoTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillDaoTestSuite::class,
    MemberDaoTestSuite::class,
)
class DataTestSuite
