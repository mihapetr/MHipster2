Feature: Feature testing

  Scenario: Automatically generate step definition stubs for undefined steps
    Given user is positioned in MHipster directory in local project files
    When user runs generate step definitions script
    Then step definition stubs are created

  Scenario: Automatically run feature testing
    Given user is positioned in MHipster directory in local project files
    And user specifies which features to test
    When user runs test script
    Then integration testing based on feature files should start

  Scenario: Automatically post test report
    Given automatic feature testing was executed
    When all selected feature tests have passed
    Then test result is posted to the platform
