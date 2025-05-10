package com.maran.healthapp.data.api
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String?,
        @Query("country") country: String?,
        @Query("page") page: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("apiKey") apiKey: String
    ): NewsApiResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsApiResponse
}