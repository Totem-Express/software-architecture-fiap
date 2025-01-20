Feature: Payment Management
  As an application user
  I want to manage payments
  So that I can check payment status and process payments

  Scenario: Successfully retrieving a payment
    Given a payment exists with id "1" and status "PAID" and qrCode "qrcode-data"
    When I send a GET request to "/api/payment/1"
    Then the response status should be 200
    And the response body should contain:
      | id    | status | qrCode      |
      | 1     | PAID   | qrcode-data |

  Scenario: Failing to retrieve a payment that does not exist
    Given no payment exists with id "1"
    When I send a GET request to "/api/payment/1"
    Then the response status should be 404

  Scenario: Successfully processing a payment
    Given a payment exists with id "1"
    When I send a POST request to "/api/payment/process/1" with body:
      """
      {
        "id": 1,
        "status": "PAID"
      }
      """
    Then the response status should be 200

  Scenario: Failing to process a payment that does not exist
    Given no payment exists with id "1"
    When I send a POST request to "/api/payment/process/1" with body:
      """
      {
        "id": 1,
        "status": "FAILED"
      }
      """
    Then the response status should be 404