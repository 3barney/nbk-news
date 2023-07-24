package com.nbk.test.news.domain.repositories

import com.nbk.test.news.domain.model.Source

interface SourceRepository {

    fun getSources(): List<Source>
}