Feature: Platform statistics overview

  Background:
    Given administrator is logged in

  Scenario: Generate overview
    Given admin navigates to overview section
    And admin selects create overview option
    Then platform statistics overview is created
    And admin is overview owner

  Scenario: Inspect platform statistics
    Given admin navigates to overview section
    When admin selects details of one of the overviews
    Then admin can see overview details

