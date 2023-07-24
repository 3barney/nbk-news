package com.nbk.test.news.application.dtos

data class ArticleDTO(
    val source: ArticleSourceDTO,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)