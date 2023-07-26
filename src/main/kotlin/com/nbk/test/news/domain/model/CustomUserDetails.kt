package com.nbk.test.news.domain.model

import com.nbk.test.news.application.services.UserService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User, private val userService: UserService) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val roles = user.roles
        return roles.map { role -> SimpleGrantedAuthority(role) }
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}