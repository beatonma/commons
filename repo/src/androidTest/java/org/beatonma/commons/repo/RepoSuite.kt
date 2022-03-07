package org.beatonma.commons.repo

import org.beatonma.commons.repo.repository.RepositorySuite
import org.beatonma.commons.repo.result.ResultFlowSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RepositorySuite::class,
    ResultFlowSuite::class,
)
class RepoSuite
