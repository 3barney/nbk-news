package com.nbk.test.news.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.domain.model.User
import com.nbk.test.news.domain.repositories.UserRepository
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.BCryptHasher
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class UserRepositoryImpl() : UserRepository {

    private val USERS_FILE = "users.properties"
    private var users = mutableMapOf<String, User>()

    init {
        loadUsers()
    }

    // Load out test users from file.
    private fun loadUsers() {
        try {
            val inputStream = javaClass.classLoader.getResourceAsStream(USERS_FILE)
            val properties = Properties().apply { load(inputStream) }

            properties.forEach { (username, value) ->
                val (password, roles) = value.toString().split(",")
                val rolesList = roles.split("\\s*,\\s*")
                val userPassword = BCryptHasher.encodePassword(password)
                users[username.toString()] = User(username.toString(), userPassword, rolesList)
            }
        } catch (e: IOException) {
            throw RuntimeException("Error loading users from properties file", e)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }



    override fun getUserByUsername(username: String): User? {
        return users[username]
    }
}