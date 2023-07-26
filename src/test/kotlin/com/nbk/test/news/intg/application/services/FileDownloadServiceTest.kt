package com.nbk.test.news.intg.application.services

import com.nbk.test.news.application.services.FileDownloadService
import com.nbk.test.news.infrastructure.adapter.outgoing.repository.FileDownloaderImpl
import com.nbk.test.news.infrastructure.exception.FileDownloadException
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FileDownloadServiceTest {

    @InjectMocks
    lateinit var fileDownloadService: FileDownloadService

    @Mock
    lateinit var fileDownloader: FileDownloaderImpl

    @Test
    fun `testDownloadFileSuccess`() {
        val fileUrl = "https://example.com/sample.pdf"
        val fileData = "This is a sample PDF file".toByteArray()

        // Mock the behavior of fileDownloader.downloadFile
        doReturn(fileData).`when`(fileDownloader).downloadFile(anyString())

        val downloadedFile = fileDownloadService.downloadFile(fileUrl)

        assertThat(downloadedFile, `is`(fileData))
    }

    @Test
    fun `testDownloadFileThrowsException`() {
        val fileUrl = "https://example.com/sample.pdf"

        // Mock the behavior of fileDownloader.downloadFile to throw an exception
        doThrow(FileDownloadException("error.file.noDownload")).`when`(fileDownloader).downloadFile(anyString())

        val exception = assertThrows<FileDownloadException> {
            fileDownloadService.downloadFile(fileUrl)
        }

        assertThat(exception.message, `is`("error.file.noDownload"))
    }
}