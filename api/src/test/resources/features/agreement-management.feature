Feature: Agreement Management
  As a user of the order management system
  I want to manage agreements
  So that I can maintain contractual relationships with parties

  Background:
    Given the system has standard agreement types configured
    And I am authenticated in the system

  Scenario: Create sales agreement
    Given I have a customer relationship established
    When I create a new sales agreement with the following details:
      | Field              | Value                          |
      | Agreement Date     | 2024-01-15                     |
      | Effective From     | 2024-02-01                     |
      | Effective Until    | 2025-01-31                     |
      | Description        | Annual Sales Agreement 2024    |
      | Agreement Text     | Standard terms and conditions  |
    Then the sales agreement should be created successfully
    And the agreement type should be "Sales Agreement"

  Scenario: Add agreement items
    Given I have an existing agreement
    When I add the following agreement items:
      | Sequence | Type                    | Text                           |
      | 1        | Agreement Section       | Pricing Terms                  |
      | 2        | Agreement Pricing       | Volume Discount Schedule       |
      | 3        | Agreement Section       | Delivery Terms                 |
    Then the agreement items should be added successfully
    And the items should maintain their sequence order
    And parent-child relationships should be supported

  Scenario: Apply agreement terms
    Given I have an agreement with items
    When I add the following terms:
      | Term Type       | Value    | Effective From | Effective Until |
      | Payment Terms   | Net 30   | 2024-02-01     | 2025-01-31      |
      | Discount Rate   | 5%       | 2024-02-01     | 2025-01-31      |
      | Minimum Order   | 1000.00  | 2024-02-01     | 2025-01-31      |
    Then the agreement terms should be added successfully
    And the terms should have valid date ranges

  Scenario: Define agreement geographic applicability
    Given I have a sales agreement
    When I specify the agreement applies to:
      | Geographic Region |
      | North America     |
      | Europe           |
    Then the geographic applicability should be recorded
    And the agreement should only apply in specified regions

  Scenario: Define agreement product applicability
    Given I have a sales agreement
    When I specify the agreement applies to:
      | Product Category |
      | Widget A Series  |
      | Widget B Series  |
    Then the product applicability should be recorded
    And the agreement should only apply to specified products

  Scenario: Assign agreement roles
    Given I have an existing agreement
    When I assign the following roles:
      | Role Type          | Party              |
      | Agreement Signer   | John CEO           |
      | Agreement Manager  | Jane Sales         |
      | Legal Reviewer     | Bob Legal          |
    Then the agreement roles should be assigned successfully

  Scenario: Create agreement addendum
    Given I have an active agreement
    When I create an addendum with the following details:
      | Field           | Value                      |
      | Creation Date   | 2024-06-01                 |
      | Effective Date  | 2024-07-01                 |
      | Text            | Modified payment terms     |
    Then the addendum should be created successfully
    And it should be linked to the original agreement

  Scenario: Link agreement to price components
    Given I have an agreement with pricing items
    When I link specific price components:
      | Price Component    | Agreement Item          |
      | Volume Discount    | Pricing Program Item    |
      | Loyalty Discount   | Pricing Program Item    |
    Then the price components should be linked
    And pricing calculations should use agreement terms

  Scenario: Create purchase agreement
    Given I have a supplier relationship established
    When I create a new purchase agreement with the following details:
      | Field              | Value                          |
      | Agreement Date     | 2024-01-20                     |
      | Effective From     | 2024-02-01                     |
      | Description        | Supplier Agreement 2024        |
    Then the purchase agreement should be created successfully
    And the agreement type should be "Purchase Agreement"

  Scenario: Search agreements by criteria
    Given I have multiple agreements in the system
    When I search for agreements with the following criteria:
      | Field           | Value              |
      | Type            | Sales Agreement    |
      | Status          | Active             |
      | Party           | ABC Corporation    |
    Then I should receive a list of matching agreements
    And I should see effective date ranges
    And I should be able to view agreement details