package org.beatonma.commons.data

import org.beatonma.commons.data.dao.DaoTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DaoTestSuite::class,
)
class DataTestSuite
