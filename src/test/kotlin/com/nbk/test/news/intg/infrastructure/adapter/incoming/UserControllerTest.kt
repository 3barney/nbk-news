package com.nbk.test.news.intg.infrastructure.adapter.incoming

import com.fasterxml.jackson.databind.ObjectMapper
import com.nbk.test.news.application.dtos.*
import com.nbk.test.news.application.services.UserService
import com.nbk.test.news.domain.model.CustomUserDetails
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.JwtUtil
import com.nbk.test.news.infrastructure.exception.UserBadCredentialsException
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var authManager: AuthenticationManager

    @MockBean
    private lateinit var jwtUtil: JwtUtil

    @Test
    fun `test login endpoint`() {
        val mockUser = com.nbk.test.news.domain.model.User("user1", "password", mutableListOf())
        val request = LoginRequestDTO("user1", "password")
        val userDetails = CustomUserDetails(mockUser, userService)
        val accessToken = "mock_access_token"

        `when`(authManager.authenticate(UsernamePasswordAuthenticationToken("user1", "password"))).thenReturn(
            UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.authorities)
        )

        `when`(jwtUtil.generateToken("user1")).thenReturn(accessToken)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{\"username\":\"user1\",\"token\":\"mock_access_token\"}"))
    }

    @Test
    fun `test login endpoint with bad credentials`() {
        val request = LoginRequestDTO("user1", "invalid_password")

        `when`(authManager.authenticate(UsernamePasswordAuthenticationToken("user1", "invalid_password")))
            .thenThrow(UserBadCredentialsException("Bad credentials"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(request))
        )
            .andExpect(status().isUnauthorized)
    }
}