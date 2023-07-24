package com.nbk.test.news.application.dtos

import com.nbk.test.news.shared.utils.BaseApiResponse


data class NewsSourcesResponseDTO(
    val sources: List<NewsSourceDTO>,
    override val totalResults: Int,
    override val status: String,
): BaseApiResponse(status, totalResults)

data class NewsSourceDTO(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)