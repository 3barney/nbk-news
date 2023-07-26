package com.nbk.test.news.infrastructure.adapter.outgoing.authentication

import com.nbk.test.news.domain.model.CustomUserDetails
import com.nbk.test.news.application.services.UserService
import com.nbk.test.news.domain.model.User
import com.nbk.test.news.infrastructure.exception.UserNotFoundException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userService: UserService,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): CustomUserDetails? {
        val user: User? = userService.getUserByUsername(username)
        return user?.let { CustomUserDetails(it, userService) }
            ?: throw UserNotFoundException("user.error.notFound")
    }
}