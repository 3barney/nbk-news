package com.nbk.test.news.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.domain.model.Article
import com.nbk.test.news.domain.repositories.ArticleRepository
import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import org.springframework.stereotype.Component

@Component
class ArticleRepositoryImpl(
    private val newsApiClient: NewsApiClient,
): ArticleRepository {
    override fun getTopHeadlines(country: String): List<Article> {
        val response = newsApiClient.getTopHeadlines(country);

        return response.articles!!
    }
}