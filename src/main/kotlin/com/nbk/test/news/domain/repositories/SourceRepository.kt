package com.nbk.test.news.domain.repositories

import com.nbk.test.news.shared.utils.NewsSourcesApiResponse

interface SourceRepository {

    fun getSources(): NewsSourcesApiResponse
}