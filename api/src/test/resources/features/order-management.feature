Feature: Order Management
  As a user of the order management system
  I want to create and manage orders
  So that I can track sales and purchase transactions

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
    And the order should have status "Created"

  Scenario: Add items to an existing order
    Given I have an existing sales order
    When I add the following items to the order:
      | Product       | Quantity | Unit Price | Delivery Date |
      | Widget A      | 10       | 25.00      | 2024-02-01    |
      | Widget B      | 5        | 50.00      | 2024-02-01    |
    Then the order items should be added successfully
    And the order total should be calculated correctly
    And each item should have a unique sequence number

  Scenario: Apply discount to order
    Given I have a sales order with items
    When I apply a 10% discount to the entire order
    Then an order adjustment of type "Discount Adjustment" should be created
    And the order total should reflect the discount

  Scenario: Calculate sales tax for order
    Given I have a sales order with items
    And the customer has a billing address in a taxable region
    When I calculate the order total
    Then sales tax should be automatically calculated
    And an order adjustment of type "Sales Tax" should be created

  Scenario: Update order status
    Given I have an existing order
    When I change the order status to "Processing"
    Then the order status should be updated
    And the status change should be timestamped
    And the status history should be maintained

  Scenario: Create a purchase order
    Given I have supplier information available
    When I create a new purchase order with the following details:
      | Field              | Value          |
      | Order Date         | 2024-01-20     |
      | Supplier           | XYZ Supplies   |
      | Delivery Location  | Warehouse A    |
    Then the purchase order should be created successfully
    And the order type should be "purchase"

  Scenario: Link sales order item to purchase order item
    Given I have a sales order item for a product
    And I have a purchase order item for the same product
    When I create an association between the items
    Then the order item association should be created
    And I should be able to track fulfillment

  Scenario: Assign order roles
    Given I have an existing order
    When I assign the following roles:
      | Role Type        | Party           | Contribution |
      | Sales Rep        | John Smith      | 100%         |
      | Order Processor  | Jane Doe        | 100%         |
    Then the order roles should be assigned successfully
    And each role should reference the correct party

  Scenario: Set order terms
    Given I have an existing order
    When I add the following terms:
      | Term Type        | Value    |
      | Payment Terms    | Net 30   |
      | Delivery Terms   | FOB      |
    Then the order terms should be added successfully
    And the terms should be retrievable with the order

  Scenario: Search orders by criteria
    Given I have multiple orders in the system
    When I search for orders with the following criteria:
      | Field        | Value      |
      | Order Type   | sales      |
      | Date Range   | Last Month |
      | Status       | Processing |
    Then I should receive a list of matching orders
    And the results should be paginated
    And I should be able to sort the results