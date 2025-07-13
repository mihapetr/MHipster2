Feature: Overview of personal projects

  Background:
    Given user is logged in : personal

  Scenario: Owned projects list
    When user navigates to projects view : personal
    Then user can see all projects they own

  Scenario: Feature test
    Given user selects a project they own
    When user selects project statistics of projects from list
    Then user can see project code statistics
