package com.nbk.test.news.intg.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.ArticleRepositoryImpl
import com.nbk.test.news.infrastructure.exception.NewsApiException
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import com.nbk.test.news.utils.generateTestArticleHeadlines
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ArticleRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var articleRepository: ArticleRepositoryImpl

    @MockBean
    private lateinit var newsApiClient: NewsApiClient

    @Test
    fun `test getTopHeadlines with successful response`() {
        val country = "us"
        val mockApiResponse = TopHeadlinesApiResponse(
            status = "ok",
            totalResults = 2,
            articles = generateTestArticleHeadlines()
        )

        `when`(newsApiClient.getTopHeadlines(country)).thenReturn(mockApiResponse)

        val result = articleRepository.getTopHeadlines(country)

        assertNotNull(result)
        assertEquals("ok", result.status)
        assertEquals(2, result.totalResults)
        assertEquals(2, result.articles.size)
    }

    @Test(expected = NewsApiException::class)
    fun `test getTopHeadlines with HttpClientErrorException NOT_FOUND`() {

        val country = "us"
        `when`(newsApiClient.getTopHeadlines(country))
            .thenThrow(NewsApiException(status = "error", errorMessageKey = "Not Found", results = 0))

        articleRepository.getTopHeadlines(country)
    }

    @Test(expected = NewsApiException::class)
    fun `test getTopHeadlines with other HttpClientErrorException`() {

        val country = "us"
        `when`(newsApiClient.getTopHeadlines(country))
            .thenThrow(NewsApiException(status = "error", errorMessageKey = "Unknown error", results = 0))

        articleRepository.getTopHeadlines(country)
    }

    @Test(expected = NewsApiException::class)
    fun `test getTopHeadlines with restTemplate returning null`() {

        val country = "us"
        `when`(newsApiClient.getTopHeadlines(country))
            .thenThrow(NewsApiException(status = "error", errorMessageKey = HttpStatus.INTERNAL_SERVER_ERROR.name, results = 0))

        articleRepository.getTopHeadlines(country)
    }
}