package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.AuthResponseDTO
import com.nbk.test.news.application.dtos.LoginRequestDTO
import com.nbk.test.news.domain.model.CustomUserDetails
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.JwtUtil
import com.nbk.test.news.infrastructure.exception.UserBadCredentialsException
import com.nbk.test.news.shared.utils.UserNotFoundResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/login")
@Validated
class UserController(
    private val authManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    @Operation(
        summary = "Login to application and get token",
        description = "Login to application and get token",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "successfully",
                content = arrayOf(
                    Content(schema = Schema(implementation = AuthResponseDTO::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "500",
                description = "error",
                content = arrayOf(
                    Content(schema = Schema(implementation = UserNotFoundResponse::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            )
        ]
    )
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