package org.beatonma.commons.network.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WikiService {
    companion object {
        const val ENDPOINT = "https://en.wikipedia.org"
    }

//    /**
//     * Example:
//     * ?pithumbsize defines the max height (px) of the returned image
//     * https://en.wikipedia.org/w/api.php?action=query&titles=Albert%20Einstein&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=500
//     */
//    @GET("w/api.php?action=query&format=json&prop=pageimages&piprop=thumbnail")
//    suspend fun getImage(
//        @Query("titles") pageTitle: String,
//        @Query("pithumbsize") maxSize: Int
//    ): Call<ResponseBody>

    /**
     * Example:
     * https://en.wikipedia.org/api/rest_v1/page/summary/Albert_Einstein
     */
    @GET("api/rest_v1/page/summary/{title}")
    suspend fun getSummary(@Path("title") pageTitle: String): Call<ResponseBody>

    /**
     * Use this if the above methods return a disambiguation page
     * Example:
     * https://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=Afzal%20Khan
     */
    @GET("w/api.php?action=opensearch&format=json")
    suspend fun search(@Query("search") query: String): Call<ResponseBody>
}
