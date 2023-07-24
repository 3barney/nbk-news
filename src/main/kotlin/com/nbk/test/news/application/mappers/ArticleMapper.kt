package com.nbk.test.news.application.mappers

import com.nbk.test.news.application.dtos.ArticleDTO
import com.nbk.test.news.application.dtos.ArticleSourceDTO
import com.nbk.test.news.domain.model.Article
import org.springframework.stereotype.Component

@Component
class ArticleMapper {
    fun mapToDTO(article: Article) = ArticleDTO(
        source = ArticleSourceDTO(
            id = article.source.id,
            name = article.source.name
        ),
        author = article.author,
        title = article.title,
        description = article.description,
        url = article.url,
        urlToImage = article.urlToImage,
        publishedAt = article.publishedAt,
        content = article.content
    )
}