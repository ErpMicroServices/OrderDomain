@bdd
Feature: Create Orders
  As a user of the order management system
  I want to create sales and purchase orders
  So that I can track business transactions

  Background:
    Given the system has standard order types configured
    And I am authenticated in the system

  Scenario: Create a new sales order
    Given I have customer information available
    When I create a new sales order with the following details:
      | Field              | Value           |
      | Order Date         | 2024-01-15      |
      | Customer           | ABC Corporation |
      | Billing Location   | 123 Main St     |
    Then the order should be created successfully
    And the order should have a unique identifier
    And the order should have status "CREATED"
    And the order type should be "SALES"

  Scenario: Create a new purchase order
    Given I have supplier information available
    When I create a new purchase order with the following details:
      | Field              | Value          |
      | Order Date         | 2024-01-20     |
      | Supplier           | XYZ Supplies   |
      | Delivery Location  | Warehouse A    |
    Then the order should be created successfully
    And the order should have a unique identifier
    And the order should have status "CREATED"
    And the order type should be "PURCHASE"

  Scenario: Validate required fields for order creation
    When I attempt to create an order without required fields
    Then the order creation should fail
    And I should receive validation error messages
    And no order should be persisted in the system