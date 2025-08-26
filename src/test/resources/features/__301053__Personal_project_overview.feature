Feature: Overview of personal projects

  Background:
    Given user is logged in : personal

  Scenario: Owned projects list
    When user navigates to projects view : personal
    Then user can see all projects they own

  Scenario: Feature test
    Given user navigates to feature tests view
    When user selects a feature test from list
    Then user can see code statistics of features from that test
