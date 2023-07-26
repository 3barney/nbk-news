package com.nbk.test.news.infrastructure.adapter.outgoing.repository

import com.nbk.test.news.domain.repositories.FileDownloader
import com.nbk.test.news.infrastructure.exception.FileDownloadException
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class FileDownloaderImpl(
    private val restTemplate: RestTemplate
): FileDownloader {
    override fun downloadFile(fileUrl: String): ByteArray {
        val responseEntity = restTemplate.exchange(
            fileUrl,
            HttpMethod.GET,
            null,
            ByteArray::class.java
        )
        return responseEntity.body ?: throw FileDownloadException("error.file.noDownload")
    }

}