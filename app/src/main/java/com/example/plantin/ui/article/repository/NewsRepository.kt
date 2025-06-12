package com.example.plantin.ui.article.repository

import com.example.plantin.ui.article.api.RetrofitInstance
import com.example.plantin.ui.article.dataclass.Article

class NewsRepository {
    private val apiKey = "e03177a4878740c4ab2a56de17269fee"

    suspend fun fetchNews(): List<Article> {
        val response = RetrofitInstance.api.getNews(apiKey = apiKey)
        return response.articles
    }
}
