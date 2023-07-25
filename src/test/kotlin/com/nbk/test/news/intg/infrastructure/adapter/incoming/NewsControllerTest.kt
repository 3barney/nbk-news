package com.nbk.test.news.intg.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.NewsSourcesResponseDTO
import com.nbk.test.news.application.dtos.TopHeadlinesResponseDTO
import com.nbk.test.news.application.mappers.ArticleMapper
import com.nbk.test.news.application.mappers.NewsSourceMapper
import com.nbk.test.news.application.services.SourcesService
import com.nbk.test.news.application.services.TopHeadlinesService
import com.nbk.test.news.utils.generateTestArticleHeadlines
import com.nbk.test.news.utils.generateTestSources
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var newsSourceMapper: NewsSourceMapper

    @Autowired
    private lateinit var articleMapper: ArticleMapper

    @MockBean
    private lateinit var topHeadlinesService: TopHeadlinesService

    @MockBean
    private lateinit var sourcesService: SourcesService

    @Test
    fun `test getTopHeadlines endpoint`() {
        val mockArticles = generateTestArticleHeadlines().map { articleMapper.mapToDTO(it)  }

        `when`(topHeadlinesService.getTopHeadlines("us")).thenReturn(
            TopHeadlinesResponseDTO(
                status = "ok",
                totalResults = mockArticles.size,
                articles = mockArticles
            )
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/news/top-headlines")
            .param("country", "us")
            .contentType(MediaType.APPLICATION_JSON))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ok"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalResults").value(mockArticles.size))
            .andExpect(MockMvcResultMatchers.jsonPath("$.articles").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.articles[0].source.id").value("reuters"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.articles[0].title").value("Ipsum dolor sit"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.articles[0].description").value("This is a test description"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.articles[1].source.id").value("cnn"))

        verify(topHeadlinesService).getTopHeadlines("us")
    }


    @Test
    fun `test getSources endpoint`() {
        val mockSources = generateTestSources().map { newsSourceMapper.mapToDTO(it) }

        `when`(sourcesService.getSources()).thenReturn(
            NewsSourcesResponseDTO(
                status = "ok",
                totalResults = mockSources.size,
                sources = mockSources
            )
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/sources")
            .contentType(MediaType.APPLICATION_JSON))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ok"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalResults").value(mockSources.size))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sources").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.sources[0].id").value("spiegel-online"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sources[0].name").value("Spiegel Online"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sources[1].id").value("svenska-dagbladet"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sources[1].name").value("Svenska Dagbladet"))

        verify(sourcesService).getSources()
    }

    @Test
    fun `test getTopHeadlines endpoint with null country`() {
        val mockArticles = generateTestArticleHeadlines().map { articleMapper.mapToDTO(it) }
        `when`(topHeadlinesService.getTopHeadlines("us")).thenReturn(
            TopHeadlinesResponseDTO(
                status = "ok",
                totalResults = mockArticles.size,
                articles = mockArticles
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/news/top-headlines")
                .param("country", null) // Pass null country here
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expect a bad request response

        verifyNoInteractions(topHeadlinesService)
    }

    @Test
    fun `test getTopHeadlines endpoint with blank country`() {
        val mockArticles = generateTestArticleHeadlines().map { articleMapper.mapToDTO(it) }
        `when`(topHeadlinesService.getTopHeadlines("us")).thenReturn(
            TopHeadlinesResponseDTO(
                status = "ok",
                totalResults = mockArticles.size,
                articles = mockArticles,
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/news/top-headlines")
                .param("country", "") // Pass blank country here
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is5xxServerError) // Expect a bad request response

        verifyNoInteractions(topHeadlinesService)
    }
}