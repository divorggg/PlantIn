package com.example.plantin.ui.article.api

import com.example.plantin.ui.article.dataclass.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String = "pertanian",
        @Query("language") language: String = "id",
        

        @Query("apiKey") apiKey: String
    ): NewsResponse
}
