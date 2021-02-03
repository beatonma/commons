package org.beatonma.commons.ukparliament.api

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.ukparliament.converters.NestedPayload
import org.beatonma.commons.ukparliament.models.UkApiBillPublication
import retrofit2.Response
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
    @NestedPayload
    suspend fun getBillPublicationDetails(
        @Path(Contract.PARLIAMENTDOTUK) billId: ParliamentID
    ): Response<List<UkApiBillPublication>>
}
