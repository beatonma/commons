package org.beatonma.commons

import org.beatonma.commons.app.AppTestSuite
import org.beatonma.commons.kotlin.RecursiveKtTest
import org.beatonma.commons.network.NetworkTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AppTestSuite::class,
    NetworkTestSuite::class,
    RecursiveKtTest::class,
)
class CommonsTestSuite
