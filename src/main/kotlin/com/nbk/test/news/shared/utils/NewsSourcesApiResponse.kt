package com.nbk.test.news.shared.utils

import com.nbk.test.news.domain.model.NewsSource

data class NewsSourcesApiResponse(
    override val status: String,
    override val totalResults: Int,
    val sources: List<NewsSource>,
    var message: String? = null
): BaseApiResponse(status, totalResults)