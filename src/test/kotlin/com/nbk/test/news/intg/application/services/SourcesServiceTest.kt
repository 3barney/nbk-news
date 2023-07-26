package com.nbk.test.news.intg.application.services

import com.nbk.test.news.application.mappers.NewsSourceMapper
import com.nbk.test.news.application.services.SourcesService
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.SourceRepositoryImpl
import com.nbk.test.news.shared.utils.NewsSourcesApiResponse
import com.nbk.test.news.utils.generateTestSources
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
class SourcesServiceIntegrationTest {

    @MockBean
    private lateinit var sourceRepository: SourceRepositoryImpl

    @Autowired
    private lateinit var newsSourceMapper: NewsSourceMapper

    @Autowired
    private lateinit var sourcesService: SourcesService

    @Test
    fun `test getSources`() {
        val mockSources = generateTestSources()

        `when`(sourceRepository.getSources()).thenReturn(
            NewsSourcesApiResponse(
                status = "ok",
                totalResults = mockSources.size,
                sources = mockSources
            )
        )

        val result = sourcesService.getSources()

        assertEquals("ok", result.status)
        assertEquals(mockSources.size, result.totalResults)
        assertEquals(mockSources.size, result.sources.size)

        for (i in 0 until mockSources.size) {
            assertEquals(mockSources[i].id, result.sources[i].id)
            assertEquals(mockSources[i].name, result.sources[i].name)
            assertEquals(mockSources[i].description, result.sources[i].description)
        }
    }

}