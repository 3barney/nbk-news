package com.nbk.test.news.domain.repositories

import com.nbk.test.news.domain.model.User

interface UserRepository {

    fun getUserByUsername(username: String): User?
}