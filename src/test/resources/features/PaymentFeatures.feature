Feature: Payment Management
  As an application user
  I want to manage payments
  So that I can check payment status and process payments

  Scenario: Retrieve an existing payment successfully
    Given a payment exists with id 1, status "PENDING", and qrCode "qrcode-data"
    When I send a GET request to "/api/payment/1"
    Then response status should be 200

  Scenario: Fail to retrieve a non-existent payment
    Given no payment exists with id 2
    When I send a GET request to "/api/payment/2"
    Then response status should be 404

  Scenario: Process an existing payment successfully
    Given a payment exists with id 1, status "PENDING", and qrCode "qrcode-data"
    When I send a POST request to "/api/payment/process/1" with body:
    """
    {
      "id": 1,
      "status": "PAID"
    }
    """
    Then response status should be 200

  Scenario: Fail to process a non-existent payment
    Given no payment exists with id 2
    When I send a POST request to "/api/payment/process/2" with body:
    """
    {
      "id": 2,
      "status": "PAID"
    }
    """
    Then response status should be 404
