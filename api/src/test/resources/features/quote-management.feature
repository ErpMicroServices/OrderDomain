Feature: Quote Management
  As a user of the order management system
  I want to create and manage quotes
  So that I can provide pricing information to customers

  Background:
    Given the system has standard quote types configured
    And I am authenticated in the system

  Scenario: Create a product quote
    Given I have a prospective customer
    When I create a new product quote with the following details:
      | Field           | Value           |
      | Issue Date      | 2024-01-10      |
      | Valid From      | 2024-01-10      |
      | Valid Until     | 2024-02-10      |
      | Customer        | ABC Corporation |
      | Description     | Q1 Product Quote|
    Then the quote should be created successfully
    And the quote should have a unique identifier
    And the quote type should be "Product Quote"

  Scenario: Add items to quote
    Given I have an existing quote
    When I add the following items to the quote:
      | Product      | Quantity | Unit Price | Delivery Date | Comment              |
      | Widget A     | 100      | 24.00      | 2024-02-15    | Bulk discount applied|
      | Widget B     | 50       | 48.00      | 2024-02-15    | Standard pricing     |
    Then the quote items should be added successfully
    And each item should have a unique sequence number
    And the quote total should be calculated

  Scenario: Apply terms to quote
    Given I have a quote with items
    When I add the following terms:
      | Term Type      | Value         |
      | Payment Terms  | Net 30        |
      | Validity       | 30 days       |
      | Delivery       | FOB Origin    |
    Then the quote terms should be added successfully
    And the terms should be associated with the quote

  Scenario: Create proposal from RFP
    Given I have received an RFP (Request for Proposal)
    When I create a proposal quote in response
    Then the quote type should be "Proposal"
    And the quote should reference the original request
    And all requested items should be addressed

  Scenario: Convert quote to order
    Given I have an accepted quote
    When I convert the quote to an order
    Then a new sales order should be created
    And all quote items should be transferred to order items
    And the quote should be linked to the order

  Scenario: Assign quote roles
    Given I have an existing quote
    When I assign the following roles:
      | Role Type        | Party          |
      | Prepared By      | John Smith     |
      | Approved By      | Jane Manager   |
      | Sales Rep        | Bob Wilson     |
    Then the quote roles should be assigned successfully
    And each role should be trackable

  Scenario: Create quote revision
    Given I have an existing quote
    When I create a revision of the quote
    Then a new quote should be created
    And it should reference the original quote
    And the revision number should be incremented

  Scenario: Search quotes by criteria
    Given I have multiple quotes in the system
    When I search for quotes with the following criteria:
      | Field          | Value         |
      | Customer       | ABC Corp      |
      | Date Range     | This Quarter  |
      | Status         | Valid         |
    Then I should receive a list of matching quotes
    And expired quotes should not be included
    And I should see quote totals and validity dates