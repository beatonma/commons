package org.beatonma.commons.network.retrofit

import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.network.retrofit.converters.EnvelopePayload
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val API_PATH = "/api"
private const val MEMBER_API_PATH = "$API_PATH/member"
private const val FEATURED_API_PATH = "$API_PATH/featured"
private const val BILL_API_PATH = "$API_PATH/bill"

interface CommonsService {
    companion object {
        const val ENDPOINT = "https://snommoc.org"
    }

    @GET("$API_PATH/ping/")
    suspend fun ping(): Response<String>

    @GET("$MEMBER_API_PATH/profile/{$PARLIAMENTDOTUK}/")
    suspend fun getMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiCompleteMember>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/members/")
    suspend fun getFeaturedPeople(): Response<List<MemberProfile>>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/bills/")
    suspend fun getFeaturedBills(): Response<List<Bill>>

    @GET("$BILL_API_PATH/{$PARLIAMENTDOTUK}")
    suspend fun getBill(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<CompleteBill>
}
