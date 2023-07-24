package com.nbk.test.news.application.mappers

import com.nbk.test.news.application.dtos.NewsSourceDTO
import com.nbk.test.news.domain.model.NewsSource
import org.springframework.stereotype.Component

@Component
class NewsSourceMapper {
    fun mapToDTO(newsSource: NewsSource) = NewsSourceDTO(
        id = newsSource.id,
        name = newsSource.name,
        description = newsSource.description,
        url = newsSource.url,
        category = newsSource.category,
        language = newsSource.language,
        country = newsSource.country
    )
}