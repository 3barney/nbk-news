package com.nbk.test.news.infrastructure.adapter.outgoing.newsapi

import com.nbk.test.news.domain.model.NewsSource
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

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
                ?: TopHeadlinesApiResponse(
                    status = "error",
                    message = "Unknown error",
                    totalResults = 0,
                    articles = emptyList()
                )
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> TopHeadlinesApiResponse(
                    status = "error",
                    message = "Not Found",
                    totalResults = 0,
                    articles = emptyList()
                )

                else -> TopHeadlinesApiResponse(
                    status = "error",
                    message = ex.message ?: "Unknown error",
                    totalResults = 0,
                    articles = emptyList()
                )
            }
        }
    }

    fun getSources(): NewsSourcesApiResponse {
        return try {

            val res = restTemplate
                .getForObject(
                    "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                    String::class.java
                )


            restTemplate
                .getForObject(
                    "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                    NewsSourcesApiResponse::class.java
                )
                ?: NewsSourcesApiResponse(
                    status = "error",
                    message = "Unknown error",
                    totalResults = 0,
                    sources = emptyList()
                )
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> NewsSourcesApiResponse(
                    status = "error",
                    message = "Not Found",
                    totalResults = 0,
                    sources = emptyList()
                )

                else -> NewsSourcesApiResponse(
                    status = "error",
                    message = ex.message ?: "Unknown error",
                    totalResults = 0,
                    sources = emptyList()
                )
            }
        }
    }

}