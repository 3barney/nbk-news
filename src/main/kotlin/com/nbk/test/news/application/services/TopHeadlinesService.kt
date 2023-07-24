package com.nbk.test.news.application.services

import com.nbk.test.news.application.dtos.ArticleDTO
import com.nbk.test.news.application.mappers.ArticleMapper
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.ArticleRepositoryImpl
import org.springframework.stereotype.Service

@Service
class TopHeadlinesService(
    private val articleRepository: ArticleRepositoryImpl,
    private val articleMapper: ArticleMapper
) {

    fun getTopHeadlines(country: String): List<ArticleDTO> {
        return articleRepository
            .getTopHeadlines(country)
            .map { articleMapper.mapToDTO(it) }
    }
}