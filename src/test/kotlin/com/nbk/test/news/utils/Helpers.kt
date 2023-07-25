package com.nbk.test.news.utils

import com.nbk.test.news.domain.model.Article
import com.nbk.test.news.domain.model.ArticleSource
import com.nbk.test.news.domain.model.NewsSource

fun generateTestSources(): List<NewsSource> {
    return listOf(
        NewsSource(
            id = "spiegel-online",
            name = "Spiegel Online",
            description = "Deutschlands führende Nachrichtenseite. Alles Wichtige aus Politik",
            url = "http://www.spiegel.de",
            category = "general",
            language = "de",
            country = "de"
        ),
        NewsSource(
            id = "svenska-dagbladet",
            name = "Svenska Dagbladet",
            description = "Sveriges ledande mediesajt - SvD.se. Svenska Dagbladets",
            category = "general",
            url = "http://www.svd.se",
            language = "sv",
            country = "se"
        ),
    )
}

fun generateTestArticleHeadlines(): List<Article> {
    return listOf(
        Article(
            source = ArticleSource(id = "reuters", name = "Reuters"),
            author = "Lorem",
            title = "Ipsum dolor sit",
            description = "This is a test description",
            url = "https://www.reuters.com/world/",
            urlToImage = "https://www.reuters.com/world/",
            publishedAt = "2023-07-23T14:11:00Z",
            content = "ODESA, July 23 (Reuters) - A Russian air attack on Ukraine's southern"
        ),
        Article(
            source = ArticleSource(id = "cnn", name = "Cnn"),
            author = "Lorem",
            title = "Ipsum dolor sit",
            description = "This is a test description",
            url = "https://www.cnn.com/world/",
            urlToImage = "https://www.cnn.com/world/",
            publishedAt = "2023-07-23T14:11:00Z",
            content = "ODESA, July 23 (Reuters) - A Russian air attack on Ukraine's southern"
        )
    )
}