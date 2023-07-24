package com.nbk.test.news.domain.repositories

import com.nbk.test.news.domain.model.Article

interface ArticleRepository {

    fun getTopHeadlines(country: String): List<Article>
}