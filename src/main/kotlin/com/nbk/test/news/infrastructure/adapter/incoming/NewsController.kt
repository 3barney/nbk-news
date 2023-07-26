package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.NewsSourcesResponseDTO
import com.nbk.test.news.application.dtos.TopHeadlinesResponseDTO
import com.nbk.test.news.application.services.SourcesService
import com.nbk.test.news.application.services.TopHeadlinesService
import com.nbk.test.news.infrastructure.exception.ValidationException
import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Headlines", description = "Fetch top news headlines per country")
@RestController
@RequestMapping("api/v1/news")
@Validated
class NewsController(
    private val topHeadlinesService: TopHeadlinesService,
    private val sourcesService: SourcesService
) {

    val logger: Logger = LoggerFactory.getLogger(NewsController::class.java)

    @Operation(
        summary = "Get headlines by country code",
        description = "Get country headlines using 2-letter ISO 3166-1 code of the country",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "successfully",
                content = arrayOf(
                    Content(schema = Schema(implementation = TopHeadlinesResponseDTO::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "500",
                description = "error",
                content = arrayOf(
                    Content(schema = Schema(implementation = TopHeadlinesApiResponse::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            )
        ]
    )
    @GetMapping("/top-headlines")
    fun getTopHeadlines(
        @RequestParam("country") country: String
    ): ResponseEntity<TopHeadlinesResponseDTO> {

        if (country.isBlank() || !isValidCountryFormat(country)) {
            logger.error("country cannot be blank { $country }")
            throw ValidationException("validation.country.blank")
        }

        val articles = topHeadlinesService.getTopHeadlines(country)
        return ResponseEntity.ok(articles)
    }


    @Operation(
        summary = "Get news sources",
        description = "Get sources of news used within the application",
        security = [SecurityRequirement(name = "bearer-key")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "successfully",
                content = arrayOf(
                    Content(schema = Schema(implementation = NewsSourcesResponseDTO::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "500",
                description = "error",
                content = arrayOf(
                    Content(schema = Schema(implementation = TopHeadlinesApiResponse::class), mediaType = "application/json")
                )
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            )
        ]
    )
    @GetMapping("/sources")
    fun getSources(): ResponseEntity<NewsSourcesResponseDTO> {
        val sources = sourcesService.getSources()
        return ResponseEntity.ok(sources)
    }

    // allow only alphabetic characters, use 2-letter ISO 3166-1 code of the country
    protected fun isValidCountryFormat(country: String): Boolean {
        return country.matches(Regex("[A-Za-z]+")) && (country.length == 2)
    }
}