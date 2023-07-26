package com.nbk.test.news.application.services

import com.nbk.test.news.infrastructure.adapter.outgoing.repository.FileDownloaderImpl
import org.springframework.stereotype.Service

@Service
class FileDownloadService(
    private val fileRepository: FileDownloaderImpl
) {

    fun downloadFile(fileUrl: String): ByteArray {
        return fileRepository.downloadFile(fileUrl)
    }
}