Feature: Logging a wine
  Scenario: Logging a new wine
    Given I am viewing a wine #1 with tags "Red Wine", "France" and "Sweet" as user 1
    When I rate it a 5
    And I select the tag "Red Wine"
    And I leave the review description "I really like this wine"
    And I click submit log
    Then The log is submitted successfully

  Scenario: Logging a new wine without selecting tag and leaving review description
    Given I am viewing a wine #2 with tags "White wine", "Germany" and "Bitter" as user 2
    When I rate it a 5
    And I click submit log
    Then The log is submitted successfully