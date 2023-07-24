package com.nbk.test.news.application.services

import com.nbk.test.news.application.dtos.TopHeadlinesResponseDTO
import com.nbk.test.news.application.mappers.ArticleMapper
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.ArticleRepositoryImpl
import org.springframework.stereotype.Service

@Service
class TopHeadlinesService(
    private val articleRepository: ArticleRepositoryImpl,
    private val articleMapper: ArticleMapper
) {

    fun getTopHeadlines(country: String): TopHeadlinesResponseDTO {
        return articleRepository
            .getTopHeadlines(country)
            .run {
                TopHeadlinesResponseDTO(
                    status = status,
                    totalResults = totalResults,
                    articles = articles.map { articleMapper.mapToDTO(it) }
                )
            }
    }

}