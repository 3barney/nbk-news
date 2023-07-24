package com.nbk.test.news.domain.repositories

import com.nbk.test.news.domain.model.NewsSource

interface SourceRepository {

    fun getSources(): List<NewsSource>
}