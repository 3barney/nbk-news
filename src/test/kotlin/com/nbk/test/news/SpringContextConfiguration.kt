package com.nbk.test.news

import io.cucumber.junit.CucumberOptions
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [NewsApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@CucumberContextConfiguration
@CucumberOptions(plugin = ["pretty",
    "json:reports/cucumber-reports/Cucumber.json",
    "io.yfarich.cucmber.reporting.MasterthoughtReportPlugin:reports/cucumber-reports/html"
], features = ["src/test/resources/features"])
class SpringContextConfiguration {}
