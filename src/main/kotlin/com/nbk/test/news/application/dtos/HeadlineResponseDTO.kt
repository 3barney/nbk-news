package com.nbk.test.news.application.dtos

import com.nbk.test.news.shared.utils.BaseApiResponse

data class TopHeadlinesResponseDTO(
    val articles: List<ArticleDTO>,
    override val totalResults: Int,
    override val status: String,
): BaseApiResponse(status, totalResults)

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

data class ArticleSourceDTO(
    val id: String?,
    val name: String?,
)
