package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.services.FileDownloadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "File Downloader", description = "Download file from a given url")

@RestController
@RequestMapping("api/v1/file-download")
@Validated
class FileController(
    private val fileDownloadService: FileDownloadService
) {

    @Operation(
        summary = "Download file from a given url",
        description = "Download file from a given url",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @GetMapping
    fun fileDownload(@RequestParam fileUrl: String): ResponseEntity<ByteArrayResource> {
        val fileData = fileDownloadService.downloadFile(fileUrl)

        // Set the response headers for file download
        val headers = HttpHeaders()
        headers.apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
            contentLength  = fileData.size.toLong()
            contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileUrl.substring(fileUrl.lastIndexOf("/") + 1))
                .build()
        }

        return ResponseEntity(ByteArrayResource(fileData), headers, org.springframework.http.HttpStatus.OK)
    }
}