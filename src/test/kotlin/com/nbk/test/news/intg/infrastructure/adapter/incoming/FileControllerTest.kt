package com.nbk.test.news.intg.infrastructure.adapter.incoming

import com.nbk.test.news.application.services.FileDownloadService
import com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt.JwtUtil
import com.nbk.test.news.infrastructure.exception.FileDownloadException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.ResourceUtils
import java.io.FileInputStream

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @MockBean
    private lateinit var fileDownloadService: FileDownloadService

    @Test
    fun `testFileDownloadSuccess`() {
        val authenticationToken = authenticateAndGetToken();
        val fileUrl = "https://example.com/sample.pdf"
        val samplePdfContent = "This is a sample PDF file".toByteArray()

        `when`(fileDownloadService.downloadFile(fileUrl)).thenReturn(samplePdfContent)

        val result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/file-download")
                .header("Authorization", "Bearer $authenticationToken")
                .param("fileUrl", fileUrl)
        )

        result
            .andExpect(status().isOk)
            .andExpect(content().bytes(samplePdfContent))
    }

    @Test
    fun `testFileDownloadInvalidFileUrl`() {
        val authenticationToken = authenticateAndGetToken();
        val invalidFileUrl = "invalid-url"

        `when`(fileDownloadService.downloadFile(invalidFileUrl))
            .thenThrow(FileDownloadException(errorMessageKey = "error.file.noDownload"))

        val result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/file-download")
                .header("Authorization", "Bearer $authenticationToken")
                .param("fileUrl", invalidFileUrl)
        )


        result
            .andExpect(status().isInternalServerError)
            .andExpect(content().json("{\"message\":\"File could not be downloaded\",\"status\":\"error\"}"))
    }

    private fun authenticateAndGetToken(): String {
        val userDetails = User.withUsername("user1").password("password1").roles("USER").build()
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        val accessToken = jwtUtil.generateToken(userDetails.username)
        SecurityContextHolder.getContext().authentication = authentication
        return accessToken
    }
}