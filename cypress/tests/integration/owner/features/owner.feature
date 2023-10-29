Feature: Owner endpoint tests

  Scenario: GET to /owners should return 200
    Given I hit "GET" to endpoint "/owners"
    Then should return status code "200"