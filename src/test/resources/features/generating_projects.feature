Feature: Generating projects

  Background:
    Given user is logged in : generating

  Scenario: Generate feature
    Given user navigates to features view
    And user selects new feature option
    And user fills the form
    When user selects create feature
    Then feature is created
    And user is feature owner

  Scenario: Generate JDL specification
    Given user navigates to JDL specification view
    And user selects new JDL specification option
    And user fills the form
    When user selects create JDL specification
    Then JDL specification is created
    And user is JDL specification owner

  Scenario: Generate the project
    Given user navigates to projects view : generating
    And user selects new project option
    And user selects JDL specification to use from the list he owns
    And user selects feature files to use from the list he owns
    When user clicks generate project
    Then project is generated using JHipster
    And project is configured to use Cucumber and JaCoCo
    And user is project owner

  Scenario: Download the project
    Given user navigates to their projects view
    And user selects project details
    When user clicks the download button
    Then user receives project files


