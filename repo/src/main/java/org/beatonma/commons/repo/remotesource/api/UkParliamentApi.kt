package org.beatonma.commons.repo.remotesource.api

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.ukparliament.models.UkApiBillPublication

interface UkParliamentApi {
    suspend fun getBillPublications(billId: ParliamentID): IoResult<List<UkApiBillPublication>>
}
