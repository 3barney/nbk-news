package com.nbk.test.news.bdd.stepdefs

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.hamcrest.Matchers.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleStepDefs {

    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    private lateinit var response: Response

    @When("the client sends a GET request to \\/sources")
    fun the_client_sends_a_get_request_to_sources() {
        response = given()
            .`when`().get("/api/v1/news/sources")
    }

    @Then("the sources response should include sources")
    fun the_sources_response_should_include_sources() {
        response.then().body("status", equalTo("ok"))
            .body("sources", hasSize<Any>(greaterThan(0)))
    }

    @Then("the sources response status should be {int}")
    fun the_sources_response_status_should_be(statusCode: Int) {
        response.then().statusCode(statusCode)
    }
}
