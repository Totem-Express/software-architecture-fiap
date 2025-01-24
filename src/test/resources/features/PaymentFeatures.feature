Feature: Payment Management
  As an application user
  I want to manage payments
  So that I can check payment status and process payments

  Scenario: Successfully retrieving a payment
    Given a payment exists with id 1 and status "PENDING" and qrCode "qrcode-data" 
    When I send a GET request to "/api/payment/1"
    Then the payment response status should be 200

  Scenario: Failing to retrieve a payment that does not exist
      Given no payment exists with id 1:
      | id | status | qrCode      |
      |    | PAID   | qrcode-data |
      When I send a GET request to "/api/payment/1"
      Then x the payment response status should be 404

  Scenario: Successfully processing a payment
    Given xa payment exists with id 1
    When I send a POST request to "/api/payment/process/1" with body:
    """
    {
      "id": 1,
      "status": "PAID"
    }
    """
    Then ythe payment response status should be 200

  Scenario: Failing to process a payment that does not exist
    Given Xno payment exists:
    | id | status |
    |    | PAID   |
    When XI send a POST request to "/api/payment/process/3" with body:
    """
          {
            "id": 3,
            "status": "FAILED"
          }
          """
    Then Zthe payment response status should be 404