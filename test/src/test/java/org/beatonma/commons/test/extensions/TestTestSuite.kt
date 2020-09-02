package org.beatonma.commons.test.extensions

import org.beatonma.commons.test.extensions.assertions.AssertionsTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test the test module.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    AssertionsTestSuite::class,
    ReflectionTest::class,
)
class TestTestSuite
