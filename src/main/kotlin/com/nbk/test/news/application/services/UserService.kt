package com.nbk.test.news.application.services

import com.nbk.test.news.domain.model.User
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.UserRepositoryImpl
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepositoryImpl
) {

    fun getUserByUsername(username: String): User? {
        return userRepository.getUserByUsername(username)
    }
}