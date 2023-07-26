package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.services.FileDownloadService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/file-download")
@Validated
class FileController(
    private val fileDownloadService: FileDownloadService
) {

    @GetMapping
    fun fileDownload(@RequestParam fileUrl: String): ResponseEntity<Resource> {
        val fileData = fileDownloadService.downloadFile(fileUrl)
        val resource = ByteArrayResource(fileData)
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=" + fileUrl.substring(fileUrl.lastIndexOf("/")))
            .body(resource)
    }
}