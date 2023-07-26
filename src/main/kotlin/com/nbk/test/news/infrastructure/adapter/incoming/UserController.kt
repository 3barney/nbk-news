package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.AuthResponseDTO
import com.nbk.test.news.domain.model.CustomUserDetails
import com.nbk.test.news.application.dtos.LoginRequestDTO
import com.nbk.test.news.application.services.UserService
import com.nbk.test.news.domain.model.User
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.JwtUtil
import com.nbk.test.news.infrastructure.exception.UserBadCredentialsException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/login")
@Validated
class UserController(
    private val userService: UserService,
    private val authManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    companion object {
        val logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @GetMapping
    fun getUser(): User? {
        return userService.getUserByUsername("user1")
    }

    @PostMapping
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<AuthResponseDTO> {
        return try {
            val authentication = authManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )

            val userDetails = authentication.principal as CustomUserDetails
            val accessToken = jwtUtil.generateToken(userDetails.username)
            val response = AuthResponseDTO(userDetails.username, accessToken)

            ResponseEntity.ok().body(response)
        } catch (ex: BadCredentialsException) {
            throw UserBadCredentialsException(ex.localizedMessage)
        }
    }
}