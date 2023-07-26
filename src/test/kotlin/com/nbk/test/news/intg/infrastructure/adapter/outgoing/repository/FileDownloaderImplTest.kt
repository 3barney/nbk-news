package com.nbk.test.news.intg.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.infrastructure.adapter.outgoing.repository.FileDownloaderImpl
import com.nbk.test.news.infrastructure.exception.FileDownloadException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withServerError
import org.springframework.web.client.RestTemplate

@RunWith(SpringRunner::class)
@SpringBootTest
class FileDownloaderImplIntegrationTest {

    @InjectMocks
    lateinit var fileDownloader: FileDownloaderImpl

    @Mock
    lateinit var restTemplate: RestTemplate

    @Test
    fun testDownloadFileSuccess() {
        val fileUrl = "https://example.com/sample.pdf"
        val fileData = "This is a sample PDF file".toByteArray()
        val responseEntity = ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileData)

        // Mock the behavior of RestTemplate
        `when`(restTemplate.exchange(
            eq(fileUrl),
            eq(HttpMethod.GET),
            eq(null),
            eq(ByteArray::class.java)
        )).thenReturn(responseEntity)

        val downloadedFile = fileDownloader.downloadFile(fileUrl)

        assertEquals(fileData.size, downloadedFile.size)
        assertEquals(fileData, downloadedFile)
    }

    @Test
    fun testDownloadFileServerError() {
        val fileUrl = "https://example.com/sample.pdf"
        val errorResponseEntity: ResponseEntity<ByteArray> = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()

        `when`(restTemplate.exchange(
            eq(fileUrl),
            eq(HttpMethod.GET),
            eq(null),
            eq(ByteArray::class.java)
        )).thenReturn(errorResponseEntity)

        val exception = assertThrows<FileDownloadException> {
            fileDownloader.downloadFile(fileUrl)
        }

        assertThat(exception.message, `is`("error.file.noDownload"))
    }
}
