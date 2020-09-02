package org.beatonma.commons.test.extensions.assertions

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnyTest::class,
    ArraysTest::class,
    CollectionTest::class,
    MetaTest::class,
    NumbersTest::class,
)
class AssertionsTestSuite
