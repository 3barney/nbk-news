package com.nbk.test.news.domain.repositories

import com.nbk.test.news.domain.model.Article
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse

interface ArticleRepository {

    fun getTopHeadlines(country: String): TopHeadlinesApiResponse
}