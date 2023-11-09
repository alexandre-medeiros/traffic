Feature: Owner endpoint tests

  Scenario: GET to /owners should return all owners
    Given I hit "GET" to endpoint "/owners"
    Then should return all "owner"
    And should return status code "200"

  Scenario: GET to /owners/{id} should return specific owner existent in database
    Given I hit "GET" to endpoint <url>
    Then should return the "owner" existent in database with same id <id>
    And should return status code "200"

    Examples:
      | url         | id  |
      | '/owners/1' | '1' |

  Scenario: POST to /owners should create a new owner
    Given I hit "POST" to "/owners" with data:
      """
      {
        "name": "Chris Doe",
        "email": "chris14.doe@example.com",
        "phone": "555-123-1235"
      }
      """
    Then the "owner" is registred with success
    And should return status code "201"

  Scenario: PUT to /owners/{id} should update owner
    Given I hit "PUT" to "/owners/1" with data:
      """
      {
        "name": "Chris Doe",
        "email": "chris13.doe@example.com",
        "phone": "555-123-1235"
      }
      """
    Then the "owner" with id "1" is updated with success
    And should return status code "200"

  Scenario: DELETE to /owners/{id} should remove specific owner existent in database
    Given I hit "DELETE" to endpoint <url>
    Then should remove the "owner" existent in database with same id <id>
    And should return status code "204"

    Examples:
      | url         | id  |
      | '/owners/1' | '1' |