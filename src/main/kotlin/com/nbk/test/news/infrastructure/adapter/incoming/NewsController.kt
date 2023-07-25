package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.NewsSourcesResponseDTO
import com.nbk.test.news.application.dtos.TopHeadlinesResponseDTO
import com.nbk.test.news.application.services.SourcesService
import com.nbk.test.news.application.services.TopHeadlinesService
import com.nbk.test.news.infrastructure.exception.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/news")
@Validated
class NewsController(
    private val topHeadlinesService: TopHeadlinesService,
    private val sourcesService: SourcesService
) {

    val log = LoggerFactory.getLogger(NewsController::class.java)

    @GetMapping("/top-headlines")
    fun getTopHeadlines(
        @RequestParam("country") country: String
    ): ResponseEntity<TopHeadlinesResponseDTO> {

        if (country.isBlank() || !isValidCountryFormat(country)) {
            log.error("country cannot be blank { $country }")
            throw ValidationException("validation.country.blank")
        }

        val articles = topHeadlinesService.getTopHeadlines(country)
        return ResponseEntity.ok(articles)
    }

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