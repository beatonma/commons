package org.beatonma.commons.snommoc

import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.snommoc.Contract.PARLIAMENTDOTUK
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.converters.EnvelopePayload
import org.beatonma.commons.snommoc.models.*
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.social.*
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



private object Endpoints {
    const val PING = "$API_PATH/ping/"
    const val SEARCH = "$MEMBER_API_PATH/?page_size=5"
    const val MOTD = "$API_PATH/motd/"

    const val MEMBER_PROFILE = "$MEMBER_API_PATH/profile/{$PARLIAMENTDOTUK}/"
    const val VOTES_BY_MEMBER = "$MEMBER_API_PATH/votes/{${Contract.HOUSE}}/{$PARLIAMENTDOTUK}/"

    const val BILL = "$BILL_API_PATH/{$PARLIAMENTDOTUK}/"

    const val DIVISION = "$DIVISION_API_PATH/{${Contract.HOUSE}}/{$PARLIAMENTDOTUK}/"

    const val CONSTITUENCY = "$CONSTITUENCY_API_PATH/{$PARLIAMENTDOTUK}/"
    const val CONSTITUENCY_ELECTION_RESULTS =
        "$CONSTITUENCY_API_PATH/{${Contract.CONSTITUENCY_ID}}/election/{${Contract.ELECTION_ID}}/"

    const val FEATURED_MEMBERS = "$FEATURED_API_PATH/members/"
    const val FEATURED_BILLS = "$FEATURED_API_PATH/bills/"
    const val FEATURED_DIVISIONS = "$FEATURED_API_PATH/divisions/"

    const val ZEITGEIST = "$API_PATH/zeitgeist/"

    object Social {

        const val ACCOUNT = "$SOCIAL_API_PATH/account/"
        const val GAUTH = "$SOCIAL_API_PATH/auth/g/"
        const val ALL = "$SOCIAL_TARGET_PATH/all/"
        const val VOTES = "$SOCIAL_TARGET_PATH/votes/"
        const val COMMENTS = "$SOCIAL_TARGET_PATH/comments/"
    }
}


interface CommonsService: SnommocService, CommonsDataService, CommonsSocialService {
    companion object {
        const val BASE_URL = "https://snommoc.org"
        const val SERVICE_NAME = "snommoc.org"

        private fun getUrl(path: String) = "$BASE_URL$path"
        fun getMemberUrl(parliamentdotuk: ParliamentID) = getUrl("$MEMBER_API_PATH/profile/$parliamentdotuk/")
        fun getDivisionUrl(parliamentdotuk: ParliamentID) = getUrl("$DIVISION_API_PATH/$parliamentdotuk/")
        fun getBillUrl(parliamentdotuk: ParliamentID) = getUrl("$BILL_API_PATH/$parliamentdotuk/")
        fun getConstituencyUrl(parliamentdotuk: ParliamentID) = getUrl("$CONSTITUENCY_API_PATH/$parliamentdotuk/")
//        fun getSocialUrl(target: SocialTarget) = getUrl("$SOCIAL_API_PATH/${target.targetType}/${target.parliamentdotuk}/all/")
    }
}


/**
 * Basic service-orientated content. Featured content, search, that sort of thing.
 */
interface SnommocService {
    @GET(Endpoints.PING)
    suspend fun ping(): Response<String>

    @Deprecated("Use Zeitgeist")
    @EnvelopePayload
    @GET(Endpoints.FEATURED_MEMBERS)
    suspend fun getFeaturedPeople(): ListResponse<ApiMemberProfile>

    // Bills
    @Deprecated("Use Zeitgeist")
    @EnvelopePayload
    @GET(Endpoints.FEATURED_BILLS)
    suspend fun getFeaturedBills(): ListResponse<ApiBill>

    // Divisions
    @Deprecated("Use Zeitgeist")
    @EnvelopePayload
    @GET(Endpoints.FEATURED_DIVISIONS)
    suspend fun getFeaturedDivisions(): ListResponse<ApiDivision>

    /**
     * Member search by name, constituency name, current post title.
     */
    @EnvelopePayload
    @GET(Endpoints.SEARCH)
    suspend fun getSearchResults(
        @Query("search") query: String,
    ): ListResponse<MemberSearchResult>

    @Deprecated("Use Zeitgeist")
    @EnvelopePayload
    @GET(Endpoints.MOTD)
    suspend fun getMessageOfTheDay(): ListResponse<MessageOfTheDay>

    @GET(Endpoints.ZEITGEIST)
    suspend fun getZeitgeist(): Response<ApiZeitgeist>
}


/**
 * Methods for getting data about political entities.
 */
interface CommonsDataService {
    @GET(Endpoints.MEMBER_PROFILE)
    suspend fun getMember(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiCompleteMember>

    @GET(Endpoints.BILL)
    suspend fun getBill(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiBill>

    /**
     * Votes by a particular member on all divisions
     */
    @EnvelopePayload
    @GET(Endpoints.VOTES_BY_MEMBER)
    suspend fun getVotesForMember(@Path(Contract.HOUSE) house: House, @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): ListResponse<ApiMemberVote>

    @GET(Endpoints.DIVISION)
    suspend fun getDivision(@Path(Contract.HOUSE) house: House, @Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiDivision>

    @GET(Endpoints.CONSTITUENCY)
    suspend fun getConstituency(@Path(PARLIAMENTDOTUK) parliamentdotuk: ParliamentID): Response<ApiConstituency>

    @GET(Endpoints.CONSTITUENCY_ELECTION_RESULTS)
    suspend fun getConstituencyElectionResults(
        @Path(Contract.CONSTITUENCY_ID) constituencyId: ParliamentID,
        @Path(Contract.ELECTION_ID) electionId: ParliamentID,
    ): Response<ApiConstituencyElectionDetails>
}


/**
 * Methods to submit and retrieve social content - votes, comments, user management...
 */
interface CommonsSocialService {
    @FormUrlEncoded
    @POST(Endpoints.Social.GAUTH)
    suspend fun registerGoogleSignIn(
        @Field(Contract.SNOMMOC_TOKEN) googleToken: String,
    ): Response<ApiUserToken>

    @SignInRequired
    @POST(Endpoints.Social.ACCOUNT)
    suspend fun requestRenameAccount(
        @Body renameAccountRequest: RenameAccountRequest,
    ): Response<Void>

    @SignInRequired
    @HTTP(method = Http.Method.DELETE, path = Endpoints.Social.ACCOUNT, hasBody = true)
    suspend fun deleteUserAccount(
        @Body user: DeleteUserRequest,
    ): Response<Void>


    @GET(Endpoints.Social.ALL)
    suspend fun getSocialContentForTarget(
        @Path(Contract.TARGET_TYPE) targetStr: String,
        @Path(Contract.PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Query(Contract.SNOMMOC_TOKEN) snommocToken: SnommocToken?,
    ): Response<SocialContent>

    @SignInRequired
    @FormUrlEncoded
    @POST(Endpoints.Social.COMMENTS)
    suspend fun postComment(
        @Path(Contract.TARGET_TYPE) targetStr: String,
        @Path(Contract.PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Field(Contract.SNOMMOC_TOKEN) snommocToken: SnommocToken,
        @Field(Contract.TEXT) text: String,
    ): Response<Void>

    @SignInRequired
    @FormUrlEncoded
    @POST(Endpoints.Social.VOTES)
    suspend fun postVote(
        @Path(Contract.TARGET_TYPE) targetStr: String,
        @Path(Contract.PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Field(Contract.SNOMMOC_TOKEN) snommocToken: SnommocToken,
        @Field(Contract.VOTE) voteType: String,
    ): Response<Void>

    @SignInRequired
    @HTTP(method = Http.Method.DELETE, path = Endpoints.Social.VOTES, hasBody = true)
    suspend fun deleteVote(
        @Path(Contract.TARGET_TYPE) targetStr: String,
        @Path(Contract.PARLIAMENTDOTUK) parliamentdotuk: ParliamentID,
        @Body snommocToken: DeletedVote,
    ): Response<Void>
}
