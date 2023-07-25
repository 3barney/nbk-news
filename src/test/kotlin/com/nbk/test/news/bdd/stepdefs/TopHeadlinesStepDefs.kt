package com.nbk.test.news.bdd.stepdefs

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.hamcrest.Matchers.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TopHeadlinesStepDefs {

    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    private lateinit var response: Response

    @Given("the country is {string}")
    fun theCountryIs(country: String) {
        response = given().param("country", country)
            .`when`().get("/api/v1/news/top-headlines")
    }

    @When("I send a GET request to \\/api\\/v1\\/news\\/top-headlines")
    fun i_send_a_get_request_to_api_v1_news_top_headlines() {
        response = given()
            .`when`().get("/api/v1/news/top-headlines?country=us")
    }

    @When("I send a GET request to \\/api\\/v1\\/news\\/top-headlines with invalid country")
    fun i_send_a_get_request_to_api_v1_news_top_headlines_with_invalid_country() {
        response = given()
            .`when`().get("/api/v1/news/top-headlines?country=invalid")
    }

    @When("the client sends a GET request to \\/api\\/v1\\/news\\/top-headlines without a country")
    fun the_client_sends_a_get_request_to_api_v1_news_top_headlines_without_a_country() {
        response = given()
            .`when`().get("/api/v1/news/top-headlines?country=")
    }

    @Then("the response status should be {int}")
    fun theResponseStatusShouldBe(statusCode: Int) {
        response.then().statusCode(statusCode)
    }

    @Then("the response should include top headlines")
    fun theResponseShouldIncludeTopHeadlines() {
        response.then().body("status", equalTo("ok"))
            .body("articles", hasSize<Any>(greaterThan(0)))
    }

    @Then("the response should include an error message")
    fun theResponseShouldIncludeAnErrorMessage() {
        response.then().body("status", equalTo("error"))
    }

    @Then("the response should have totalResults equal to zero")
    fun theResponseShouldHaveTotalResultsEqualToZero() {
        response.then().body("totalResults", equalTo(0))
            .body("articles", hasSize<Any>(equalTo(0)))
    }
}
