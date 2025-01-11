Feature: Create User
  As an application user
  I want to create user accounts
  So that users can access the system

  Scenario: Successfully creating a user
    Given I have a valid user with:
      | name  | email                 | document          |
      | Hilary OBrian | hilary@hotmail.com | 114.974.750-15  |
    When I send a POST request to "/api/users"
    Then the response status should be 200

  Scenario: Failing to create a user with invalid input
    Given I have an invalid user with:
      | name  | email        | document |
      |       | invalid      | invalid  |
    When I send a POST request to "/api/users"
    Then the response status should be 400