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

  Scenario: GET to /owners/{id} no owner found
    Given I hit "GET" to endpoint <url>
    Then there is no "owner" in database with same id <id>
    And should return status code "404"
    And should return error instance <url>
    And should return error message "Entity 'Owner' with 'id 11' not found"

    Examples:
      | url          | id   |
      | '/owners/11' | '11' |

  Scenario: POST to /owners should create a new owner
    Given I hit "POST" to "/owners" with data:
      """
      {
        "name": "Chris Doe",
        "email": "chris.doe@example.com",
        "phone": "555-123-1235"
      }
      """
    Then the "owner" is registred with success
    And should return status code "201"

  Scenario: POST to /owners should throw error if same email to new owner
    Given I hit "POST" to "/owners" with data:
      """
      {
        "name": "Elisabeth Doe",
        "email": "chris.doe@example.com",
        "phone": "555-434-1435"
      }
      """
    Then should return error message "Owner with the same email already exists"
    And should return status code "400"
    And should return error instance "/owners"

  Scenario: PUT to /owners/{id} should update owner
    Given I hit "PUT" to "/owners/1" with data:
      """
      {
        "name": "John Doe Wick",
        "email": "john.doe.wick@example.com",
        "phone": "555-123-9999"
      }
      """
    Then the "owner" with id "1" is updated with success
    And should return status code "200"

  Scenario: PUT to /owners/{id} with owner with another user's email
    Given I hit "PUT" to "/owners/2" with data:
      """
      {
        "name": "Jane Smith Doe",
        "email": "jane.doe@example.com",
        "phone": "555-987-6543"
      }
      """
    Then the "owner" with id "2" is updated with success
    And should return status code "200"

  Scenario: DELETE to /owners/{id} should remove specific owner existent in database
    Given I hit "DELETE" to endpoint <url>
    Then should remove the "owner" existent in database with same id <id>
    And should return status code "204"

    Examples:
      | url          | id   |
      | '/owners/10' | '10' |