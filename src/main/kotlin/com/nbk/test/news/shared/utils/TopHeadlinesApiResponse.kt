package com.nbk.test.news.shared.utils

import com.nbk.test.news.domain.model.Article

data class TopHeadlinesApiResponse(
    override val status: String,
    override val totalResults: Int,
    val articles: List<Article>,
    var message: String? = null
): BaseApiResponse(status, totalResults)