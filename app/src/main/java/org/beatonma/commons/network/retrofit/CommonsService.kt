package org.beatonma.commons.network.retrofit

import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.data.ListResponse
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.SnommocToken
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.MessageOfTheDay
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.room.entities.bill.ApiBill
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.constituency.ApiConstituency
import org.beatonma.commons.data.core.room.entities.constituency.ApiConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiMemberVote
import org.beatonma.commons.data.core.room.entities.member.BasicProfile
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.ApiUserToken
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.data.core.social.DeletedVote
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.network.retrofit.converters.EnvelopePayload
import retrofit2.Response
import retrofit2.http.*

private const val API_PATH = "/api"
private const val MEMBER_API_PATH = "$API_PATH/member"
private const val FEATURED_API_PATH = "$API_PATH/featured"
private const val BILL_API_PATH = "$API_PATH/bill"
private const val DIVISION_API_PATH = "$API_PATH/division"
private const val CONSTITUENCY_API_PATH = "$API_PATH/constituency"
private const val SOCIAL_API_PATH = "/social"
private const val SOCIAL_TARGET_PATH = "$SOCIAL_API_PATH/{target_type}/{parliamentdotuk}"

private const val TOKEN = "token"
private const val TARGET_TYPE = "target_type"


interface CommonsService: SocialCommonsService {
    companion object {
        const val BASE_URL = "https://snommoc.org"

        private fun getUrl(path: String) = "$BASE_URL$path"
        fun getMemberUrl(parliamentdotuk: ParliamentID) = getUrl("$MEMBER_API_PATH/profile/$parliamentdotuk/")
        fun getDivisionUrl(parliamentdotuk: ParliamentID) = getUrl("$DIVISION_API_PATH/$parliamentdotuk/")
        fun getBillUrl(parliamentdotuk: ParliamentID) = getUrl("$BILL_API_PATH/$parliamentdotuk/")
        fun getConstituencyUrl(parliamentdotuk: ParliamentID) = getUrl("$CONSTITUENCY_API_PATH/$parliamentdotuk/")
        fun getSocialUrl(target: SocialTarget) = getUrl("$SOCIAL_API_PATH/${target.targetType}/${target.parliamentdotuk}/all/")
    }

    @GET("$API_PATH/ping/")
    suspend fun ping(): Response<String>

    // Members
    @GET("$MEMBER_API_PATH/profile/{$PARLIAMENTDOTUK}/")
    suspend fun getMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiCompleteMember>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/members/")
    suspend fun getFeaturedPeople(): ListResponse<MemberProfile>

    // Bills
    @EnvelopePayload
    @GET("$FEATURED_API_PATH/bills/")
    suspend fun getFeaturedBills(): ListResponse<Bill>

    @GET("$BILL_API_PATH/{$PARLIAMENTDOTUK}/")
    suspend fun getBill(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiBill>

    // Divisions
    @EnvelopePayload
    @GET("$FEATURED_API_PATH/divisions/")
    suspend fun getFeaturedDivisions(): ListResponse<ApiDivision>

    /**
     * Votes by a particular member on all divisions
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/votes/commons/{$PARLIAMENTDOTUK}/")
    suspend fun getCommonsVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): ListResponse<ApiMemberVote>

    /**
     * Votes by a particular member on all divisions
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/votes/lords/{$PARLIAMENTDOTUK}/")
    suspend fun getLordsVotesForMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): ListResponse<ApiMemberVote>

    @GET("$DIVISION_API_PATH/commons/{$PARLIAMENTDOTUK}/")
    suspend fun getCommonsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiDivision>

    @GET("$DIVISION_API_PATH/lords/{$PARLIAMENTDOTUK}/")
    suspend fun getLordsDivision(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiDivision>

    // Constituencies
    @GET("$CONSTITUENCY_API_PATH/{$PARLIAMENTDOTUK}/")
    suspend fun getConstituency(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiConstituency>

    @GET("$CONSTITUENCY_API_PATH/member/{$PARLIAMENTDOTUK}/")
    suspend fun getMemberForConstituency(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<BasicProfile>

    @GET("$CONSTITUENCY_API_PATH/{constituency_id}/election/{election_id}/")
    suspend fun getConstituencyElectionResults(
        @Path("constituency_id") constituencyId: ParliamentID,
        @Path("election_id") electionId: ParliamentID,
    ): Response<ApiConstituencyElectionDetails>

    /**
     * Member search by name, constituency name, current post title.
     */
    @EnvelopePayload
    @GET("$MEMBER_API_PATH/?page_size=5")
    suspend fun getSearchResults(@Query("search") query: String): ListResponse<MemberSearchResult>

    @EnvelopePayload
    @GET("$API_PATH/motd/")
    suspend fun getMessageOfTheDay(): ListResponse<MessageOfTheDay>
}


interface SocialCommonsService {

    @FormUrlEncoded
    @POST("$SOCIAL_API_PATH/auth/g/")
    suspend fun registerGoogleSignIn(@Field(TOKEN) googleToken: String): Response<ApiUserToken>

    @GET("$SOCIAL_TARGET_PATH/all/")
    suspend fun getSocialContentForTarget(
        @Path(TARGET_TYPE) targetStr: String,
        @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Query(TOKEN) snommocToken: SnommocToken?,
    ): Response<SocialContent>

    @SignInRequired
    @FormUrlEncoded
    @POST("$SOCIAL_TARGET_PATH/comments/")
    suspend fun postComment(

        @Path(TARGET_TYPE) targetStr: String,
        @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Field(TOKEN) snommocToken: SnommocToken,
        @Field("text") text: String,
    ): Response<Void>

    @SignInRequired
    @FormUrlEncoded
    @POST("$SOCIAL_TARGET_PATH/votes/")
    suspend fun postVote(
        @Path(TARGET_TYPE) targetStr: String,
        @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Field(TOKEN) snommocToken: SnommocToken,
        @Field("vote") voteType: String,
    ): Response<Void>

    @SignInRequired
    @HTTP(method = "DELETE", path = "$SOCIAL_TARGET_PATH/votes/", hasBody = true)
    suspend fun deleteVote(
        @Path(TARGET_TYPE) targetStr: String,
        @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Body snommocToken: DeletedVote,
    ): Response<Void>
}
