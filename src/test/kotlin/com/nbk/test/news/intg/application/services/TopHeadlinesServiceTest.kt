package com.nbk.test.news.intg.application.services

import com.nbk.test.news.application.mappers.ArticleMapper
import com.nbk.test.news.application.services.TopHeadlinesService
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.ArticleRepositoryImpl
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import com.nbk.test.news.utils.generateTestArticleHeadlines
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class TopHeadlinesServiceIntegrationTest {

    @MockBean
    private lateinit var articleRepository: ArticleRepositoryImpl

    @Autowired
    private lateinit var articleMapper: ArticleMapper

    @Autowired
    private lateinit var topHeadlinesService: TopHeadlinesService

    @Test
    fun `test getTopHeadlines`() {
        val mockArticles = generateTestArticleHeadlines()

        `when`(articleRepository.getTopHeadlines("us")).thenReturn(
            TopHeadlinesApiResponse(
                status = "ok",
                totalResults = mockArticles.size,
                articles = mockArticles
            )
        )

        val result = topHeadlinesService.getTopHeadlines("us")

        assertEquals("ok", result.status)
        assertEquals(mockArticles.size, result.totalResults)
        assertEquals(mockArticles.size, result.articles.size)

        for (i in 0 until mockArticles.size) {
            assertEquals(mockArticles[i].source.id, result.articles[i].source.id)
            assertEquals(mockArticles[i].title, result.articles[i].title)
            assertEquals(mockArticles[i].description, result.articles[i].description)
        }
    }

}