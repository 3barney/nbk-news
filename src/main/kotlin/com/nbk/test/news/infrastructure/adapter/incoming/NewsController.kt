package com.nbk.test.news.infrastructure.adapter.incoming

import com.nbk.test.news.application.dtos.ArticleDTO
import com.nbk.test.news.application.dtos.NewsSourceDTO
import com.nbk.test.news.application.services.SourcesService
import com.nbk.test.news.application.services.TopHeadlinesService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/news")
class NewsController(
    private val topHeadlinesService: TopHeadlinesService,
    private val sourcesService: SourcesService
) {

    @GetMapping("/top-headlines")
    fun getTopHeadlines(@RequestParam("country") country: String): ResponseEntity<List<ArticleDTO>> {
        val articles = topHeadlinesService.getTopHeadlines(country)
        return ResponseEntity.ok(articles)
    }

    @GetMapping("/sources")
    fun getSources(): ResponseEntity<List<NewsSourceDTO>> {
        val sources = sourcesService.getSources()
        return ResponseEntity.ok(sources)
    }
}