package com.nbk.test.news.application.services

import com.nbk.test.news.application.dtos.NewsSourceDTO
import com.nbk.test.news.application.mappers.NewsSourceMapper
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.SourceRepositoryImpl
import org.springframework.stereotype.Service

@Service
class SourcesService(
    private val sourceRepository: SourceRepositoryImpl,
    private val newsSourceMapper: NewsSourceMapper
) {

    fun getSources(): List<NewsSourceDTO> {
        return sourceRepository
            .getSources()
            .map { newsSourceMapper.mapToDTO(it) }
    }
}