package com.nbk.test.news.intg.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.SourceRepositoryImpl
import com.nbk.test.news.infrastructure.exception.NewsApiException
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import com.nbk.test.news.utils.generateTestSources
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

class SourceRepositoryImplTest {

    @Autowired
    private lateinit var sourceRepository: SourceRepositoryImpl

    @MockBean
    private lateinit var newsApiClient: NewsApiClient

    @Test
    fun `test getSources  with successful response`() {

        val mockApiResponse = NewsSourcesApiResponse(
            status = "ok",
            totalResults = 2,
            sources = generateTestSources()
        )

        `when`(newsApiClient.getSources()).thenReturn(mockApiResponse)

        val result = sourceRepository.getSources()

        assertNotNull(result)
        assertEquals("ok", result.status)
        assertEquals(2, result.totalResults)
        assertEquals(2, result.sources.size)
    }

    @Test(expected = NewsApiException::class)
    fun `test getSources with HttpClientErrorException NOT_FOUND`() {

        `when`(newsApiClient.getSources())
            .thenThrow(NewsApiException(status = "error", errorMessage = "Not Found", results = 0))

        sourceRepository.getSources()
    }

    @Test(expected = NewsApiException::class)
    fun `test getTopHeadlines with other HttpClientErrorException`() {

        `when`(newsApiClient.getSources())
            .thenThrow(NewsApiException(status = "error", errorMessage = "Unknown error", results = 0))

        sourceRepository.getSources()
    }

    @Test(expected = NewsApiException::class)
    fun `test getTopHeadlines with restTemplate returning null`() {

        `when`(newsApiClient.getSources())
            .thenThrow(NewsApiException(status = "error", errorMessage = HttpStatus.INTERNAL_SERVER_ERROR.name, results = 0))

        sourceRepository.getSources()
    }
}