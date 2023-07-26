package com.nbk.test.news.domain.repositories

interface FileDownloader {
    fun downloadFile(fileUrl: String): ByteArray
}