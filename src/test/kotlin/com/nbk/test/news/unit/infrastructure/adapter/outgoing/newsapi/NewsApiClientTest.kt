package com.nbk.test.news.unit.infrastructure.adapter.outgoing.newsapi

import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import com.nbk.test.news.infrastructure.exception.NewsApiException
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import com.nbk.test.news.utils.generateTestArticleHeadlines
import com.nbk.test.news.utils.generateTestSources
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@RunWith(MockitoJUnitRunner::class)
class NewsApiClientTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private var baseUrl = "https://newsapi.org/v2"
    private var apiKey = "test-api-key"

    private lateinit var newsApiClient: NewsApiClient

    @Before
    fun setUp() {
        newsApiClient = NewsApiClient(restTemplate, baseUrl, apiKey)
    }

    @Test
    fun `test getTopHeadlines with successful response`() {
        val country = "us"
        val mockApiResponse = TopHeadlinesApiResponse(
            status = "ok",
            totalResults = 2,
            articles = generateTestArticleHeadlines()
        )
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines?country=$country&apiKey=$apiKey",
                TopHeadlinesApiResponse::class.java
            )
        ).thenReturn(mockApiResponse)

        val result = newsApiClient.getTopHeadlines(country)

        assertNotNull(result)
        assertEquals("ok", result.status)
        assertEquals(2, result.totalResults)
        assertEquals(2, result.articles.size)
    }

    @Test
    fun `test getTopHeadlines with null response`() {
        val country = "us"
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines?country=$country&apiKey=$apiKey",
                TopHeadlinesApiResponse::class.java
            )
        ).thenReturn(null)

        try {
            newsApiClient.getTopHeadlines(country)
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("Unknown error", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }

    @Test
    fun `test getTopHeadlines with HttpClientErrorException NOT_FOUND`() {
        val country = "us"
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines?country=$country&apiKey=$apiKey",
                TopHeadlinesApiResponse::class.java
            )
        ).thenThrow(HttpClientErrorException(HttpStatus.NOT_FOUND))

        try {
            newsApiClient.getTopHeadlines(country)
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("Not Found", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }

    @Test
    fun `test getTopHeadlines with other HttpClientErrorException`() {
        val country = "us"
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines?country=$country&apiKey=$apiKey",
                TopHeadlinesApiResponse::class.java
            )
        ).thenThrow(HttpClientErrorException(HttpStatus.BAD_REQUEST))

        try {
            newsApiClient.getTopHeadlines(country)
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("400 BAD_REQUEST", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }

    @Test
    fun `test getSources with successful response`() {
        val mockApiResponse = NewsSourcesApiResponse(
            status = "ok",
            totalResults = 2,
            sources = generateTestSources()
        )
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                NewsSourcesApiResponse::class.java
            )
        ).thenReturn(mockApiResponse)

        val result = newsApiClient.getSources()

        assertNotNull(result)
        assertEquals("ok", result.status)
        assertEquals(2, result.totalResults)
        assertEquals(2, result.sources.size)
    }

    @Test
    fun `test getSources with null response`() {
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                NewsSourcesApiResponse::class.java
            )
        ).thenReturn(null)

        try {
            newsApiClient.getSources()
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("Unknown error", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }

    @Test
    fun `test getSources with HttpClientErrorException NOT_FOUND`() {
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                NewsSourcesApiResponse::class.java
            )
        ).thenThrow(HttpClientErrorException(HttpStatus.NOT_FOUND))

        try {
            newsApiClient.getSources()
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("Not Found", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }

    @Test
    fun `test getSources with other HttpClientErrorException`() {
        `when`(
            restTemplate.getForObject(
                "$baseUrl/top-headlines/sources?apiKey=$apiKey",
                NewsSourcesApiResponse::class.java
            )
        ).thenThrow(HttpClientErrorException(HttpStatus.BAD_REQUEST))

        try {
            newsApiClient.getSources()
            fail("Expected NewsApiException was not thrown.")
        } catch (ex: NewsApiException) {
            assertEquals("error", ex.status)
            assertEquals("400 BAD_REQUEST", ex.errorMessage)
            assertEquals(0, ex.results)
        }
    }
}