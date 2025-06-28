Feature: Requirement Management
  As a user of the order management system
  I want to manage requirements
  So that I can track customer needs and internal demands

  Background:
    Given the system has standard requirement types configured
    And I am authenticated in the system

  Scenario: Create customer requirement
    Given I have a customer with specific needs
    When I create a new customer requirement with the following details:
      | Field               | Value                        |
      | Description         | Need 500 units of Widget A   |
      | Required By         | 2024-03-01                   |
      | Estimated Budget    | 12000.00                     |
      | Quantity            | 500                          |
      | Reason              | Seasonal demand increase     |
    Then the requirement should be created successfully
    And the requirement type should be "Customer Requirement"
    And the creation date should be today

  Scenario: Create internal requirement
    Given I need to maintain inventory levels
    When I create a new internal requirement with the following details:
      | Field               | Value                        |
      | Description         | Restock Widget B inventory   |
      | Required By         | 2024-02-15                   |
      | Quantity            | 200                          |
      | Facility            | Warehouse A                  |
      | Reason              | Below minimum stock level    |
    Then the requirement should be created successfully
    And the requirement type should be "Internal Requirement"

  Scenario: Update requirement status
    Given I have an existing requirement
    When I update the requirement status to "In Progress"
    Then the requirement status should be updated
    And the status date should be recorded
    And the status history should be maintained

  Scenario: Assign requirement roles
    Given I have an existing requirement
    When I assign the following roles:
      | Role Type           | Party          | From Date   |
      | Requirement Owner   | John Smith     | 2024-01-15  |
      | Approver           | Jane Manager   | 2024-01-15  |
    Then the requirement roles should be assigned successfully
    And the role assignments should have valid date ranges

  Scenario: Link requirement to order
    Given I have an approved requirement
    And I have created an order to fulfill it
    When I create an order requirement commitment
    Then the commitment should link the requirement to the order item
    And the committed quantity should be tracked
    And the requirement fulfillment should be trackable

  Scenario: Create child requirements
    Given I have a complex requirement
    When I break it down into child requirements:
      | Description                    | Quantity | Required By |
      | Source raw materials          | 1000     | 2024-02-01  |
      | Schedule production capacity  | 500      | 2024-02-15  |
      | Arrange shipping             | 500      | 2024-02-28  |
    Then the child requirements should be created
    And they should reference the parent requirement
    And the hierarchy should be navigable

  Scenario: Track requirement fulfillment
    Given I have a requirement with multiple order commitments
    When I view the requirement details
    Then I should see the total committed quantity
    And I should see the remaining unfulfilled quantity
    And I should see all related orders

  Scenario: Search requirements by criteria
    Given I have multiple requirements in the system
    When I search for requirements with the following criteria:
      | Field           | Value              |
      | Type            | Customer           |
      | Status          | Open               |
      | Required By     | Next 30 days       |
    Then I should receive a list of matching requirements
    And I should see urgency indicators
    And I should be able to sort by required date