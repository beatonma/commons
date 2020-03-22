package org.beatonma.commons.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface GenericUrlService {
    @GET
    suspend fun get(@Url url: String): Response<String>
}
