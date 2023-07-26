package com.nbk.test.news.shared.utils

import com.nbk.test.news.domain.model.Article
import com.nbk.test.news.domain.model.NewsSource

open class BaseApiResponse(open val status: String, open val totalResults: Int)

data class NewsSourcesApiResponse(
    override val status: String,
    override val totalResults: Int,
    val sources: List<NewsSource>,
    var message: String? = null
): BaseApiResponse(status, totalResults)

data class TopHeadlinesApiResponse(
    override val status: String,
    override val totalResults: Int,
    val articles: List<Article>,
    var message: String? = null
): BaseApiResponse(status, totalResults)

data class UserNotFoundResponse(
    val message: String,
    val status: String
)

data class FileDownloadResponse(
    val message: String,
    val status: String
)