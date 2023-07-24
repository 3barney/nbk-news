package com.nbk.test.news.infrastructure.exception

import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NewsApiException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleNewsApiException(exception: NewsApiException): TopHeadlinesApiResponse {

        return TopHeadlinesApiResponse(
            status = exception.status,
            message = exception.errorMessage,
            totalResults = exception.results,
            articles = emptyList()
        )
    }
}