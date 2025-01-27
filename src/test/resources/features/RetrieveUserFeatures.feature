Feature: Retrieve User
  As an application user
  I want to retrieve user accounts
  So that I can display user information

  Scenario: Successfully retrieving a user
    Given a user exists with document "276.790.760-65" and with name "Angelina Jolie" and with email "angelina@jolie.com"
    When I send a GET request to "/api/users" with parameter "document=276.790.760-65"
    Then should return http 200
    And the response body should contain:
      | name       |
      | Angelina Jolie |

  Scenario: Failing to retrieve a user that does not exist
    Given no user exists with document "123.456.789-00"
    When I send a GET request to "/api/users" with parameter "document=123.456.789-00"
    Then should return http 404