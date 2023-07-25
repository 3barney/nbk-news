Feature: Sources

  Scenario: Retrieve sources
    When the client sends a GET request to /sources
    Then the sources response status should be 200
    And the sources response should include sources