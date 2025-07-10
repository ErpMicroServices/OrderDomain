@bdd
Feature: Order Items Management
  As a user of the order management system
  I want to add and manage items on orders
  So that I can specify what is being ordered

  Background:
    Given the system has standard order types configured
    And I am authenticated in the system
    And I have an existing sales order

  Scenario: Add items to an existing order
    When I add the following items to the order:
      | Product       | Quantity | Unit Price | Delivery Date |
      | Widget A      | 10       | 25.00      | 2024-02-01    |
      | Widget B      | 5        | 50.00      | 2024-02-01    |
    Then the order items should be added successfully
    And each item should have a unique sequence number
    And the order total should be calculated correctly

  Scenario: Update order item quantity
    Given the order has existing items
    When I update an order item quantity from 10 to 15
    Then the item quantity should be updated
    And the order total should be recalculated
    And the item modification should be tracked

  Scenario: Remove order item
    Given the order has existing items
    When I remove an order item
    Then the item should be removed from the order
    And the order total should be recalculated
    And the sequence numbers should be maintained

  Scenario: Validate order item constraints
    When I attempt to add an order item with invalid data:
      | Field          | Invalid Value |
      | Quantity       | -5            |
      | Unit Price     | -10.00        |
      | Product        | null          |
    Then the order item creation should fail
    And I should receive appropriate validation errors