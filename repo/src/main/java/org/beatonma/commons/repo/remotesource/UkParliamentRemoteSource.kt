package org.beatonma.commons.repo.remotesource

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.repo.remotesource.api.UkParliamentApi
import org.beatonma.commons.ukparliament.api.UkParliamentService
import javax.inject.Inject

class UkParliamentRemoteSource @Inject constructor(
    private val service: UkParliamentService,
): RemoteSource, UkParliamentApi {
    override suspend fun getBillPublications(billId: ParliamentID) =
        getResult {
            service.getBillPublicationDetails(billId)
        }
}
