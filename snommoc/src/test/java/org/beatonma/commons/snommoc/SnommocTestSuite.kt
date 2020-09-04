package org.beatonma.commons.snommoc

import org.beatonma.commons.snommoc.converters.RecursiveKtTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RecursiveKtTest::class,
    HttpClientTest::class,
)
class SnommocTestSuite
