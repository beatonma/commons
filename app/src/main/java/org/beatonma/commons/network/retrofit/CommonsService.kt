package org.beatonma.commons.network.retrofit

import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.room.entities.bill.ApiBill
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.constituency.ApiConstituency
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiMemberVote
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.ApiUserToken
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.network.retrofit.converters.EnvelopePayload
import retrofit2.Response
import retrofit2.http.*

private const val API_PATH = "/api"
private const val MEMBER_API_PATH = "$API_PATH/member"
private const val FEATURED_API_PATH = "$API_PATH/featured"
private const val BILL_API_PATH = "$API_PATH/bill"
private const val DIVISION_API_PATH = "$API_PATH/division"
private const val CONSTITUENCY_API_PATH = "$API_PATH/constituency"

interface CommonsService {
    companion object {
        const val BASE_URL = "https://snommoc.org"

        private fun getUrl(path: String) = "$BASE_URL$path"
        fun getMemberUrl(parliamentdotuk: Int) = getUrl("$MEMBER_API_PATH/profile/$parliamentdotuk/")
        fun getDivisionUrl(parliamentdotuk: Int) = getUrl("$DIVISION_API_PATH/$parliamentdotuk/")
        fun getBillUrl(parliamentdotuk: Int) = getUrl("$BILL_API_PATH/$parliamentdotuk/")
        fun getConstituencyUrl(parliamentdotuk: Int) = getUrl("$CONSTITUENCY_API_PATH/$parliamentdotuk/")
    }

    @GET("$API_PATH/ping/")
    suspend fun ping(): Response<String>

    // Members
    @GET("$MEMBER_API_PATH/profile/{$PARLIAMENTDOTUK}/")
    suspend fun getMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiCompleteMember>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/members/")
    suspend fun getFeaturedPeople(): Response<List<MemberProfile>>

    // Bills
    @EnvelopePayload
    @GET("$FEATURED_API_PATH/bills/")
    suspend fun getFeaturedBills(): Response<List<Bill>>

    @GET("$BILL_API_PATH/{$PARLIAMENTDOTUK}/")
    suspend fun getBill(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiBill>

    // Divisions
    @EnvelopePayload
    @GET("$FEATURED_API_PATH/divisions/")
    suspend fun getFeaturedDivisions(): Response<List<ApiDivision>>

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
    suspend fun getLordsVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<List<ApiMemberVote>>

    @GET("$DIVISION_API_PATH/commons/{$PARLIAMENTDOTUK}/")
    suspend fun getCommonsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiDivision>

    @GET("$DIVISION_API_PATH/lords/{$PARLIAMENTDOTUK}/")
    suspend fun getLordsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiDivision>

    // Constituencies
    @GET("$CONSTITUENCY_API_PATH/{$PARLIAMENTDOTUK}/")
    suspend fun getConstituency(@Path(PARLIAMENTDOTUK) parliamentdotuk: Int): Response<ApiConstituency>

    /**
     * Member search by name, constituency name, current post title.
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/?page_size=5")
    suspend fun getSearchResults(@Query("search") query: String): Response<List<MemberSearchResult>>

    @FormUrlEncoded
    @POST("/auth/g/")
    suspend fun registerGoogleSignIn(@Field("token") googleToken: String): Response<ApiUserToken>
}
