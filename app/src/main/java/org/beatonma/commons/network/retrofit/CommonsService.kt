package org.beatonma.commons.network.retrofit

import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.room.entities.bill.ApiBill
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiMemberVote
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.network.retrofit.converters.EnvelopePayload
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val API_PATH = "/api"
private const val MEMBER_API_PATH = "$API_PATH/member"
private const val FEATURED_API_PATH = "$API_PATH/featured"
private const val BILL_API_PATH = "$API_PATH/bill"
private const val DIVISION_API_PATH = "$API_PATH/division"

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

    @GET("$BILL_API_PATH/{$PARLIAMENTDOTUK}/")
    suspend fun getBill(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiBill>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/divisions/")
    suspend fun getFeaturedDivisions(): Response<List<ApiDivision>>

//    /**
//     * Votes by a particular member on all divisions
//     */
//    @GET("$MEMBER_API_PATH/votes/{$PARLIAMENTDOTUK}/")
//    suspend fun getVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<List<String>>

    /**
     * Votes by a particular member on all divisions
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/votes/commons/{$PARLIAMENTDOTUK}/")
    suspend fun getCommonsVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<List<ApiMemberVote>>


    /**
     * Votes by a particular member on all divisions
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/votes/lords/{$PARLIAMENTDOTUK}/")
    suspend fun getLordsVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<String>

    /**
     * Data about a particular division including votes by all members.
     */
//    @GET("$DIVISION_API_PATH/{$PARLIAMENTDOTUK}/")
//    suspend fun getDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiDivision>

    @GET("$DIVISION_API_PATH/commons/{$PARLIAMENTDOTUK}/")
    suspend fun getCommonsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiDivision>

    @GET("$DIVISION_API_PATH/lords/{$PARLIAMENTDOTUK}/")
    suspend fun getLordsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiDivision>

    // TODO: 06/04/2020
//    @GET("$DIVISION_API_PATH/commons/")
//    suspend fun getCommonsDivisions()

    // TODO: 06/04/2020
//    @GET("$DIVISION_API_PATH/lords/")
//    suspend fun getLordsDivisions()
}
