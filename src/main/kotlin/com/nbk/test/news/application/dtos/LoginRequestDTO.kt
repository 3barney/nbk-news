package com.nbk.test.news.application.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequestDTO(
    @JsonProperty("username") val username: String,
    @JsonProperty("password")val password: String
)