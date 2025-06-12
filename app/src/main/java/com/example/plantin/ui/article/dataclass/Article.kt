package com.example.plantin.ui.article.dataclass

data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String
)