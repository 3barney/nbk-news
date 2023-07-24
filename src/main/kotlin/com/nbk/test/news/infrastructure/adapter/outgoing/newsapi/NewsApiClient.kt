package com.nbk.test.news.infrastructure.adapter.outgoing.newsapi

import com.nbk.test.news.infrastructure.exception.NewsApiException
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class NewsApiClient(
    private val restTemplate: RestTemplate,
    @Value("\${news.api.endpoint}") private val baseUrl: String,
    @Value("\${news.api.key}") private val apiKey: String
) {

    fun getTopHeadlines(country: String): TopHeadlinesApiResponse {

        return try {
            restTemplate
                .getForObject(
                    "$baseUrl/top-headlines?country=$country&apiKey=$apiKey",
                    TopHeadlinesApiResponse::class.java
                )
                ?: throw NewsApiException (
                    status = "error",
                    errorMessage = "Unknown error",
                    results = 0,
                )
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> throw NewsApiException(
                    status = "error",
                    errorMessage = "Not Found",
                    results = 0
                )

                else -> throw NewsApiException (
                    status = "error",
                    errorMessage = ex.message ?: "Unknown error",
                    results = 0
                )
            }
        }
    }

    fun getSources(): NewsSourcesApiResponse {
        return try {
            restTemplate
                .getForObject(
                    "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                    NewsSourcesApiResponse::class.java
                )
                ?: throw NewsApiException (
                    status = "error",
                    errorMessage = "Unknown error",
                    results = 0,
                )
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> throw NewsApiException(
                    status = "error",
                    errorMessage = "Not Found",
                    results = 0
                )

                else -> throw NewsApiException(
                    status = "error",
                    errorMessage = ex.message ?: "Unknown error",
                    results = 0
                )
            }
        }
    }

}