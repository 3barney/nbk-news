package com.nbk.test.news.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.application.mappers.ArticleMapper
import com.nbk.test.news.domain.model.NewsSource
import com.nbk.test.news.domain.repositories.SourceRepository
import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class SourceRepositoryImpl(
    private val newsApiClient: NewsApiClient,
): SourceRepository {
    override fun getSources(): List<NewsSource> {
        val response = newsApiClient.getSources()

        // TODO: add  exception handler here
        return response.sources!!
    }
}