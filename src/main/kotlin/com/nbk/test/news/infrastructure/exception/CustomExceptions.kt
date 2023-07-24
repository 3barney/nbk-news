package com.nbk.test.news.infrastructure.exception

class NewsApiException(
    val status: String,
    val errorMessage: String,
    val results: Int
): RuntimeException(errorMessage)
