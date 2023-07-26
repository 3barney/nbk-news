package com.nbk.test.news.application.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponseDTO (
        @JsonProperty("username") val username: String,
        @JsonProperty("token")val token: String)
