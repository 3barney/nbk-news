package com.nbk.test.news.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.domain.repositories.SourceRepository
import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import org.springframework.stereotype.Component

@Component
class SourceRepositoryImpl(
    private val newsApiClient: NewsApiClient,
): SourceRepository {
    override fun getSources(): NewsSourcesApiResponse {
        return newsApiClient.getSources()
    }
}