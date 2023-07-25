package com.nbk.test.news.infrastructure.configuration

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val log: Logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    // Aspect method to log controller method execution and execution time
    @Around("execution(* com.nbk.test.news.infrastructure.adapter.incoming..*.*(..))")
    fun logAroundAllControllerMethods(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val methodName = proceedingJoinPoint.signature.name
        val startTime = System.currentTimeMillis()

        log.info("CONTROLLER : START execution of : $methodName")

        val response = proceedingJoinPoint.proceed()
        val executionTime = System.currentTimeMillis() - startTime

        log.info("CONTROLLER : Execution Time: $executionTime ms")
        log.info("CONTROLLER : END execution of : $methodName")

        return response
    }

    // Aspect to log entry, exit and exception for NewsAPiClient that calls external news org api
    @Around("execution(* com.nbk.test.news.infrastructure.adapter.outgoing.newsapi.NewsApiClient.*(..))")
    fun logAroundNewsApiClientMethods(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val methodName = proceedingJoinPoint.signature.name
        val startTime = System.currentTimeMillis()

        log.info("EXTERNAL NEWS API : Entering method: $methodName")

        try {
            val response = proceedingJoinPoint.proceed()
            val executionTime = System.currentTimeMillis() - startTime

            log.info("EXTERNAL NEWS API : Execution Time: $executionTime ms")
            log.info("EXTERNAL NEWS API : Method execution completed: $methodName")

            return response
        } catch (ex: Exception) {
            log.error("EXTERNAL NEWS API : Exception occurred in method: $methodName", ex)
            throw ex
        }
    }

    // Aspect to log entry, exit and exception for service classes
    @Around("execution(* com.nbk.test.news.application.services..*.*(..))")
    fun logAroundServiceMethods(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val methodName = proceedingJoinPoint.signature.name
        val startTime = System.currentTimeMillis()

        log.info("SERVICE : Entering method: $methodName")

        try {
            val response = proceedingJoinPoint.proceed()
            val executionTime = System.currentTimeMillis() - startTime

            log.info("SERVICE : Execution Time: $executionTime ms")
            log.info("SERVICE : Method execution completed: $methodName")

            return response
        } catch (ex: Exception) {
            log.error("SERVICE : Exception occurred in method: $methodName", ex)
            throw ex
        }
    }
}