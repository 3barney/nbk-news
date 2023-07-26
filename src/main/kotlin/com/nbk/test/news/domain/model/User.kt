package com.nbk.test.news.domain.model

data class User (
    val username: String,
    val password: String,
    val roles: List<String>
)