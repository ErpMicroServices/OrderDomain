Feature: Request Management
  As a user of the order management system
  I want to manage requests (RFP, RFQ, RFI)
  So that I can handle procurement and information gathering processes

  Background:
    Given the system has standard request types configured
    And I am authenticated in the system

  Scenario: Create Request for Proposal (RFP)
    Given I need to solicit proposals from vendors
    When I create a new RFP with the following details:
      | Field                   | Value                          |
      | Request Date            | 2024-01-15                     |
      | Response Required Date  | 2024-02-15                     |
      | Description             | Website Development Project    |
    Then the RFP should be created successfully
    And the request type should be "RFP"

  Scenario: Add items to request
    Given I have an existing RFP
    When I add the following request items:
      | Description              | Required By | Quantity | Max Amount |
      | Frontend Development     | 2024-03-01  | 1        | 50000.00   |
      | Backend Development      | 2024-03-15  | 1        | 75000.00   |
      | Testing & QA            | 2024-04-01  | 1        | 25000.00   |
    Then the request items should be added successfully
    And each item should be individually trackable

  Scenario: Create Request for Quote (RFQ)
    Given I need pricing for specific products
    When I create a new RFQ with the following details:
      | Field                   | Value                          |
      | Request Date            | 2024-01-20                     |
      | Response Required Date  | 2024-01-27                     |
      | Description             | Q1 Widget Pricing Request      |
    Then the RFQ should be created successfully
    And the request type should be "RFQ"

  Scenario: Send request to responding parties
    Given I have a completed RFP
    When I send the request to the following parties:
      | Party Name      | Contact Method  | Date Sent  |
      | Vendor A        | Email          | 2024-01-16 |
      | Vendor B        | Portal         | 2024-01-16 |
      | Vendor C        | Email          | 2024-01-17 |
    Then the responding party records should be created
    And each party should have contact information recorded
    And the send dates should be tracked

  Scenario: Link request to requirement
    Given I have an existing requirement
    And I have created an RFQ to fulfill it
    When I link the request item to the requirement
    Then the requirement request link should be created
    And the requirement should show related requests

  Scenario: Convert request response to quote
    Given I have received a response to an RFQ
    When I convert the response to a quote
    Then a new quote should be created
    And the quote items should match the request items
    And the quote should reference the original request

  Scenario: Assign request roles
    Given I have an existing request
    When I assign the following roles:
      | Role Type        | Party           |
      | Request Creator  | John Smith      |
      | Approver        | Jane Manager    |
      | Buyer           | Bob Purchasing  |
    Then the request roles should be assigned successfully

  Scenario: Track request responses
    Given I have sent an RFP to multiple vendors
    When I view the request status
    Then I should see which parties have responded
    And I should see which responses are pending
    And I should see days until response deadline

  Scenario: Create Request for Information (RFI)
    Given I need technical information from vendors
    When I create a new RFI with the following details:
      | Field                   | Value                          |
      | Request Date            | 2024-01-25                     |
      | Response Required Date  | 2024-02-05                     |
      | Description             | Technical Capabilities Survey  |
    Then the RFI should be created successfully
    And the request type should be "RFI"

  Scenario: Search requests by criteria
    Given I have multiple requests in the system
    When I search for requests with the following criteria:
      | Field           | Value           |
      | Type            | RFP             |
      | Status          | Open            |
      | Date Range      | This Month      |
    Then I should receive a list of matching requests
    And I should see response deadlines
    And I should see response status summary