# BDD Features Directory

This directory contains Behavior-Driven Development (BDD) feature files written in Gherkin syntax. These features are shared between frontend and backend testing to ensure consistent behavior across the entire application.

## Purpose

- **Shared Testing**: Both API and UI tests reference the same feature files
- **Consistent Behavior**: Ensures frontend and backend implement the same business logic
- **Living Documentation**: Features serve as executable documentation of system behavior
- **Cross-team Communication**: Clear, business-readable specifications

## Structure

Feature files should be organized by business domain:

```
features/
├── order-management/
│   ├── create-orders.feature
│   ├── order-items.feature
│   └── order-adjustments.feature
├── quote-management/
│   ├── create-quotes.feature
│   └── quote-conversions.feature
├── requirement-management/
├── request-management/
└── agreement-management/
```

## Implementation Approach

1. **Create features during implementation**: Don't create feature files until you're ready to implement that specific functionality
2. **Write failing tests first**: Create the feature file, then implement step definitions that fail
3. **Implement to make tests pass**: Build the actual functionality to satisfy the scenarios
4. **Test from both sides**: Use the same feature file for both API integration tests and UI end-to-end tests

## Usage in Testing

### API Module (Cucumber with Spring Boot)
- Step definitions in `api/src/test/java/steps/`
- Reference features using: `features = "classpath:../../../features"`
- Integration tests with TestContainers

### UI Components (Frontend Testing)
- Step definitions for UI automation
- Reference same feature files for consistency
- End-to-end testing with browser automation

## Guidelines

- Write scenarios in business language, not technical terms
- Focus on behavior, not implementation details
- Keep scenarios independent and atomic
- Use data tables for multiple test cases
- Include both happy path and error scenarios

## Example Feature Structure

```gherkin
Feature: Order Creation
  As a sales representative
  I want to create orders for customers
  So that I can track sales transactions

  Background:
    Given I am authenticated as a sales representative
    And the system has standard order types configured

  Scenario: Create a basic sales order
    Given I have customer "ABC Corp" information
    When I create a sales order with:
      | Field        | Value      |
      | Order Date   | 2024-01-15 |
      | Customer     | ABC Corp   |
    Then the order should be created successfully
    And the order should have a unique identifier
```

This ensures that both frontend and backend teams implement the same business logic and user experience.