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

  Scenario: Re-logging a previously logged wine
    Given I am viewing a wine #2 with tags "Pinor", "Italy" and "2006" as user 3
    When I have previously rated it a 5 with the description "I love this wine"
    And I rate it a -1
    And I click submit log
    Then The log is properly updated