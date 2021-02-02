package org.beatonma.commons.ukparliament.api

import org.beatonma.commons.core.ParliamentID
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Path parameter representing a ParliamentID.
 */
private const val ID = "{${Contract.PARLIAMENTDOTUK}}"

private object Endpoints {
    const val BILL_PUBLICATIONS = "/bills/$ID/publications.json"
}


/**
 * Service for use with the UK Parliament Linked Data API.
 */
interface UkParliamentService {
    companion object {
        const val BASE_URL = "https://lda.data.parliament.uk"
    }
    
    // Sample: https://lda.data.parliament.uk/bills/393258/publications.json
    @GET(Endpoints.BILL_PUBLICATIONS)
    suspend fun getBillPublicationDetails(
        @Path(Contract.PARLIAMENTDOTUK) billId: ParliamentID
    ): List<Any>
}


