package org.beatonma.commons.network.retrofit

import org.beatonma.commons.data.core.Person
import org.beatonma.commons.data.core.room.entities.MemberProfile
import org.beatonma.commons.network.retrofit.converters.EnvelopePayload
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

private const val API_PATH = "/api"
private const val MEMBER_API_PATH = "$API_PATH/member"
private const val FEATURED_API_PATH = "$API_PATH/featured"

interface CommonsService {
    companion object {
        const val ENDPOINT = "https://snommoc.org"
    }

    @GET("$API_PATH/ping/")
    suspend fun ping(): Response<String>

    @GET("$MEMBER_API_PATH/profile/{parliamentdotuk}/")
    suspend fun getProfile(@Path("parliamentdotuk") parliamentdotuk: Int): Response<Person>

    @EnvelopePayload
    @GET("$FEATURED_API_PATH/members/")
    suspend fun getFeaturedPeople(): Response<List<MemberProfile>>
}
