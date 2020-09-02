package org.beatonma.commons.repo

import org.beatonma.commons.repo.deserialization.DeserializationTestSuite
import org.beatonma.commons.repo.repository.CommonsRepositoryTestSuite
import org.beatonma.commons.repo.result.ResultFlowTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CommonsRepositoryTestSuite::class,
    DeserializationTestSuite::class,
    ResultFlowTestSuite::class,
)
class RepoTestSuite
