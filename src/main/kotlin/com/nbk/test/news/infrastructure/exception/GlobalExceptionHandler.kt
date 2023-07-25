package com.nbk.test.news.infrastructure.exception

import com.nbk.test.news.shared.utils.TopHeadlinesApiResponse
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {

    val locale = LocaleContextHolder.getLocale();

    @ExceptionHandler(NewsApiException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleNewsApiException(exception: NewsApiException): TopHeadlinesApiResponse {

        val errorMessage = messageSource.getMessage(exception.errorMessageKey, null, locale)
        return TopHeadlinesApiResponse(
            status = exception.status,
            message = errorMessage,
            totalResults = exception.results,
            articles = emptyList()
        )
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleValidationException(exception: ValidationException): TopHeadlinesApiResponse {

        val errorMessage = messageSource.getMessage(exception.errorMessageKey, null, locale)
        return TopHeadlinesApiResponse(
            status = "error",
            message = errorMessage,
            totalResults = 0,
            articles = emptyList()
        )
    }

}