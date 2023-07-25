Feature: Top Headlines

  Scenario: Retrieve top headlines for a valid country
    Given the country is "us"
    When I send a GET request to /api/v1/news/top-headlines
    Then the response status should be 200
    And the response should include top headlines

  Scenario: Retrieve top headlines for an invalid country
    Given the country is "invalid"
    When I send a GET request to /api/v1/news/top-headlines with invalid country
    Then the response status should be 200
    And the response should have totalResults equal to zero

  Scenario: Retrieve top headlines with no country
    When the client sends a GET request to /api/v1/news/top-headlines without a country
    Then the response status should be 500
    And the response should include an error message
