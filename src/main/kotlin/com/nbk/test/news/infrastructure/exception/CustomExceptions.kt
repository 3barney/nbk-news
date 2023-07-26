package com.nbk.test.news.infrastructure.exception

class NewsApiException(
    val status: String,
    val errorMessageKey: String,
    val results: Int
): RuntimeException(errorMessageKey)

class ValidationException(val errorMessageKey: String) : RuntimeException(errorMessageKey)
class UserNotFoundException(val errorMessageKey: String) : RuntimeException(errorMessageKey)

class UserBadCredentialsException(val errorMessageKey: String): RuntimeException(errorMessageKey)